package com.jpmc.theater.processor;


import com.jpmc.theater.model.Showing;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class DiscountCalculator {
    private static final double NO_DISCOUNT = 0;

    private static class DISCOUNT_RULES {
        private static final Function<Showing, Double> SPECIAL_MOVIE = (showing) -> showing.getMovie().isSpecial() ?
                                                                              showing.getMovie().getTicketPrice() * 0.2 : 0;
        private static final Function<Showing, Double> FIRST_SHOWING = (showing) -> showing.getSequenceOfTheDay() == 1 ? 3.0 : 0;
        private static final Function<Showing, Double> SECOND_SHOWING = (showing) -> showing.getSequenceOfTheDay() == 2 ? 1.0 : 0;
        private static final Function<Showing, Double> AFTERNOON_SHOWING = (showing) -> (showing.getShowStartTime().getHour() >= 11
                                                                                     && showing.getShowStartTime().getHour() <= 16) ?
                                                                                   showing.getMovie().getTicketPrice() * 0.25 : 0;
        private static final Function<Showing, Double> DAY_7_SHOWING = (showing) -> showing.getShowStartTime().getDayOfMonth() == 7 ? 1.0 : 0;
    }

    private final List<Function<Showing,Double>> discountRules;

    public DiscountCalculator() {
        discountRules = List.of(DISCOUNT_RULES.SPECIAL_MOVIE, DISCOUNT_RULES.FIRST_SHOWING,
                                DISCOUNT_RULES.SECOND_SHOWING, DISCOUNT_RULES.AFTERNOON_SHOWING,
                                DISCOUNT_RULES.DAY_7_SHOWING);
    }

    public double calculateHighestDiscountAmount(Showing showing) {
       validate(showing);
       Optional<Double> discount = discountRules.stream().map(rule -> rule.apply(showing)).max(Double::compareTo);
       if (discount.isPresent()) {
           double discountAmount = discount.get();
           if (discountAmount > showing.getMovie().getTicketPrice()) {
               throw new IllegalStateException("Discount can not be more than ticket price");
           }
           System.out.println("Total "+discountAmount +" discount per ticket available for the request ");
           return discountAmount;
       }
      return NO_DISCOUNT;
    }

    // can ticket cost be 0?
    private void validate(Showing showing) {
        Objects.requireNonNull(showing);
        Objects.requireNonNull(showing.getMovie());
        if(showing.getMovie().getTicketPrice() < 0) {
            throw new IllegalArgumentException("Movie ticket cost can not be less than 0");
        }
    }

}
