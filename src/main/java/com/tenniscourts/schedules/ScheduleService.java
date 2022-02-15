package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    @Autowired
    private final ScheduleRepository scheduleRepository;
    @Autowired
    private final TennisCourtRepository tennisCourtRepository;
    @Autowired
    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {

        if (createScheduleRequestDTO.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date cannot be set in the past.");
        }
        if (scheduleRepository.findByTennisCourt_IdAndStartDateTime(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO.getStartDateTime()) != null) {
            throw new AlreadyExistsEntityException("Schedule already set for this tennis court.");
        }

        Schedule schedule = Schedule.builder()
                .tennisCourt(tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid tennis court id.")))
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1))
                .build();
        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be set before end date.");
        }
        return scheduleMapper.map(scheduleRepository.findByStartDateTimeAfterAndEndDateTimeBeforeOrderByStartDateTime(startDate, endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found."));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
