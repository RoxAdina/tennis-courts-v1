package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final ScheduleRepository scheduleRepository;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {

        if (reservationRepository.findByGuest_IdAndSchedule_Id(createReservationRequestDTO.getGuestId(),
                createReservationRequestDTO.getScheduleId()) != null) {
            throw new AlreadyExistsEntityException("The guest has already reserved this schedule.");
        }

        Reservation reservation = Reservation.builder()
                .guest(guestRepository.findById(createReservationRequestDTO.getGuestId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid guest id.")))
                .schedule(scheduleRepository.findById(createReservationRequestDTO.getScheduleId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid schedule id.")))
                .value(BigDecimal.valueOf(10))
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .build();
        return reservationMapper.map(reservationRepository.save(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    public BigDecimal getRefundValue(Reservation reservation, ReservationStatus newStatus) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (newStatus.equals(ReservationStatus.CANCELLED) ||
                newStatus.equals(ReservationStatus.RESCHEDULED)) {
            return calculateRefundForCancelledOrRescheduled(reservation.getValue(), hours);
        }
        else if (newStatus.equals(ReservationStatus.FULFILLED)) {
            return reservation.getValue();
        }
        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {

        Reservation previousReservation = reservationRepository.findById(previousReservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation = cancel(previousReservationId);
        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    public List<ReservationDTO> getReservationsHistory() {
        return reservationMapper.map(reservationRepository.findBySchedule_EndDateTimeBefore(LocalDateTime.now()));
    }

    // This method is available only to the Tennis Court Admin client
    // Allows more status options than cancel and reschedule operations, which are available to the Guest client
    public ReservationDTO updateReservationStatus(Long reservationId, ReservationStatus reservationStatus) {
        return reservationMapper.map(reservationRepository.findById(reservationId).map(reservation -> {

            BigDecimal refundValue = getRefundValue(reservation, reservationStatus);
            return this.updateReservation(reservation, refundValue, reservationStatus);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        }));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation, ReservationStatus.CANCELLED);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.saveAndFlush(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    private BigDecimal calculateRefundForCancelledOrRescheduled(BigDecimal reservationValue, long hours) {
        if (hours >= 24) {
            return reservationValue;
        }
        else if (12 <= hours && hours < 24) {
            return reservationValue.multiply(BigDecimal.valueOf(0.75));
        }
        else if (2 <= hours && hours < 12) {
            return reservationValue.multiply(BigDecimal.valueOf(0.5));
        }
        else {
            return reservationValue.multiply(BigDecimal.valueOf(0.25));
        }
    }
}
