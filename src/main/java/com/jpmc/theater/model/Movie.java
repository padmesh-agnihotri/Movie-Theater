package com.jpmc.theater.model;

import lombok.Value;

import java.time.Duration;

@Value
public class Movie {

    private String title;
    private String description;
    private Duration runningTime;
    private double ticketPrice;
    private boolean isSpecial;
}
