package com.tenniscourts.utils;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.schedules.Schedule;

import java.time.LocalDateTime;

public class TestUtils {

    public static Schedule createScheduleAfterTwoDays() {
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);

        return schedule;
    }

    public static Schedule createScheduleAfterTwentyHours() {
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(20);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);

        return schedule;
    }

    public static Schedule createScheduleAfterThreeHours() {
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(3);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);

        return schedule;
    }

    public static Schedule createScheduleAfterOneHour() {
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(1);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);

        return schedule;
    }

    public static Schedule createScheduleInThePast() {
        LocalDateTime startDateTime = LocalDateTime.now().minusHours(5);
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);

        return schedule;
    }

    public static Guest createGuest() {
        return Guest.builder()
                .name("Simona Halep")
                .email("simona.halep@atp.com")
                .phoneNumber("123456789")
                .isActive(true)
                .build();
    }
}
