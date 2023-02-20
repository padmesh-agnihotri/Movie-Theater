package com.jpmc.theater.model;

import lombok.Builder;
import lombok.Value;

@Value
public class Reservation {
    private Customer customer;
    private Showing showing;
    private int ticketsBooked;
    private double totalCost;
}
