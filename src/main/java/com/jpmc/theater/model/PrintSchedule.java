package com.jpmc.theater.model;


import lombok.Value;

@Value
public class PrintSchedule {

    private int sequenceOfTheDay;
    private String showStartTime;
    private String movieTitle;
    private String runningTime;
    private double ticketPrice;

    public String toString() {
        return sequenceOfTheDay + ": " + showStartTime + " " + movieTitle
                + " " + runningTime + " $" + ticketPrice;
    }
}
