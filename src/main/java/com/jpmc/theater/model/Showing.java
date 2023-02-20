package com.jpmc.theater.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Showing {
    private Movie movie;
    private int sequenceOfTheDay;
    private LocalDateTime showStartTime;
}
