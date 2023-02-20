package com.jpmc.theater.processor;

import static java.util.Comparator.comparingInt;

import com.google.gson.Gson;
import com.jpmc.theater.model.*;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ReservationProcessor {

    private final DiscountCalculator discountCalculator;
    private final LocalDateProvider provider;
    private final Theater theater;

    public ReservationProcessor(Theater theater,LocalDateProvider provider, DiscountCalculator calculator) {
     Objects.requireNonNull(theater);
     Objects.requireNonNull(provider);
     Objects.requireNonNull(calculator);

     this.discountCalculator = calculator;
     this.provider = provider;
     this.theater = theater;
    }

    public Reservation bookReservation(Customer customer,int movieSequence,int totalTickets) {
        Objects.requireNonNull(customer);

        if (totalTickets <= 0) {
            throw new IllegalArgumentException("Total tickets must be more than 0");
        }

        Showing showing = getShowing(movieSequence);

        double costPerTicket = showing.getMovie().getTicketPrice() - discountCalculator.calculateHighestDiscountAmount(showing);

        if (costPerTicket <= 0) {
            throw new IllegalStateException("per ticket cost should be more than 0");
        }

        double totalCost = costPerTicket*totalTickets;
        System.out.println("Booking " +totalTickets+ " tickets for total cost of "+totalCost);
        return new Reservation(customer,showing,totalTickets,totalCost);
    }

    private Showing getShowing(int movieSequence) {
        List<Showing> showing = this.theater.getShowing().stream().filter(show -> show.getSequenceOfTheDay() == movieSequence).collect(Collectors.toUnmodifiableList());
        if (showing.size() >1) {
            throw new IllegalStateException("Unable to uniquely determine movie based on sequence.");
        }

        if (showing.isEmpty()) {
            throw new IllegalArgumentException("Unable to find movie with sequence "+movieSequence);
        }
      return showing.get(0);
      // is there a limit on total tickets per customer? how to check for total capacity?
    }

    public void printScheduleInText() {
        System.out.println(provider.currentDate());
        System.out.println("===================================================");
        getPrintSchedule().stream().peek(System.out::println);
        System.out.println("===================================================");
    }

    public void printScheduleInJson() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(getPrintSchedule()));
    }

    private List<PrintSchedule> getPrintSchedule() {
       return theater.getShowing().stream().sorted(comparingInt(Showing::getSequenceOfTheDay)).map(showing -> buildPrintableSchedule(showing))
                                          .collect(Collectors.toUnmodifiableList());
    }

    private PrintSchedule buildPrintableSchedule(Showing showing) {
        return new PrintSchedule(showing.getSequenceOfTheDay(),showing.getShowStartTime().toString(),showing.getMovie().getTitle(),
                humanReadableFormat(showing.getMovie().getRunningTime()),showing.getMovie().getTicketPrice());

    }

    private String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());
        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        if (value == 1) {
            return "";
        }
        else {
            return "s";
        }
    }

    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.singleton());
        ReservationProcessor reservationProcessor = new ReservationProcessor(theater,LocalDateProvider.singleton(),new DiscountCalculator());
        reservationProcessor.printScheduleInText();
        reservationProcessor.printScheduleInJson();
    }
}
