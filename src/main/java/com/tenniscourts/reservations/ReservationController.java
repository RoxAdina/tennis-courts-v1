package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/tennis-courts/reservation")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    @Autowired
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable("reservationId") Long reservationId, @RequestParam Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }

    @GetMapping(path = "/history")
    public ResponseEntity<List<ReservationDTO>> getReservationsHistory() {
        return ResponseEntity.ok(reservationService.getReservationsHistory());
    }

    @PutMapping(path = "{reservationId}/status")
    public ResponseEntity<ReservationDTO> updateReservationStatus(@PathVariable("reservationId") Long reservationId, @RequestParam ReservationStatus reservationStatus) {
        return ResponseEntity.ok(reservationService.updateReservationStatus(reservationId, reservationStatus));
    }
}
