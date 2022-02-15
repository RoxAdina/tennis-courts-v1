package com.tenniscourts.reservations;

import com.tenniscourts.utils.TestUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void testGetRefundValueFullRefund() {

        Assert.assertEquals(new BigDecimal(10),
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterTwoDays()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.CANCELLED));

        Assert.assertEquals(new BigDecimal(10),
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterTwoDays()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.RESCHEDULED));

        Assert.assertEquals(new BigDecimal(10),
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleInThePast()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.FULFILLED));
    }

    @Test
    public void testGetRefundValue75Refund() {

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterTwentyHours()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.CANCELLED).compareTo(new BigDecimal(7.5)) == 0);

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterTwentyHours()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.RESCHEDULED).compareTo(new BigDecimal(7.5)) == 0);
    }

    @Test
    public void testGetRefundValue50Refund() {

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterThreeHours()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.CANCELLED).compareTo(new BigDecimal(5)) == 0);

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterThreeHours()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.RESCHEDULED).compareTo(new BigDecimal(5)) == 0);
    }

    @Test
    public void testGetRefundValue25Refund() {

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterOneHour()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.CANCELLED).compareTo(new BigDecimal(2.5)) == 0);

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterOneHour()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.RESCHEDULED).compareTo(new BigDecimal(2.5)) == 0);
    }

    @Test
    public void testGetRefundValueNoRefund() {

        Assert.assertTrue(
                reservationService.getRefundValue(
                        Reservation.builder().schedule(TestUtils.createScheduleAfterOneHour()).value(new BigDecimal(10L)).build(),
                        ReservationStatus.NOT_SHOW).compareTo(BigDecimal.ZERO) == 0);
    }

}