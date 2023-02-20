package com.jpmc.theater.processor;

import com.jpmc.theater.model.Customer;
import com.jpmc.theater.model.Reservation;
import com.jpmc.theater.model.Theater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReservationProcessorTest {

    private static ReservationProcessor objectInTest;
    private  static Customer customer;

    @BeforeAll
    public static void setup () {
        customer = new Customer("Test","Test-Id");
        objectInTest = new ReservationProcessor(new Theater(LocalDateProvider.singleton()),LocalDateProvider.singleton(),new DiscountCalculator());
    }

    @Test
    public void customerIsNull() {
        Assertions.assertThrows(NullPointerException.class,() -> objectInTest.bookReservation(null,1,1));
    }

    @Test
    public void totalTicketsLessThanZero() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> objectInTest.bookReservation(customer,1,-1));
    }

    @Test
    public void totalTicketsAreZero() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> objectInTest.bookReservation(customer,1,0));
    }

    @Test
    public void showingIsNotAvailableForTheGivenSeq() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> objectInTest.bookReservation(customer,10000,0));
    }

    @Test
    public void ticketsAreBooked() {
        Reservation reservation = objectInTest.bookReservation(customer,1,10);
        Assertions.assertNotNull(reservation);
        Assertions.assertEquals(reservation.getTotalCost(),80.0);
        Assertions.assertEquals(reservation.getShowing().getMovie().getTitle(),"Turning Red");
        Assertions.assertEquals(reservation.getCustomer(),customer);
    }


}