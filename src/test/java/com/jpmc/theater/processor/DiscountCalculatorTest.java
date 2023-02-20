package com.jpmc.theater.processor;

import com.jpmc.theater.model.Movie;
import com.jpmc.theater.model.Showing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

// can discount be equal to ticket price?
class DiscountCalculatorTest {

    private DiscountCalculator objectInTest = new DiscountCalculator();


    @Test
    public void showingIsNull() {
        Assertions.assertThrows(Exception.class, () -> objectInTest.calculateHighestDiscountAmount(null));
    }

    @Test
    public void movieIsNull() {
        Showing showing = createShowing(null,1,LocalDateTime.of(LocalDate.of(2022,01,07), LocalTime.of(13, 0)));
        Assertions.assertThrows(Exception.class, () -> objectInTest.calculateHighestDiscountAmount(showing));
    }

    @Test
    public void discountIsMoreThanPrice() {
        Movie movie = new Movie("Spider-Man: No Way Home", "Super hero movie",
                Duration.ofMinutes(90), 2, true);
        Showing showing = createShowing(movie,1,LocalDateTime.of(LocalDate.of(2022,01,07), LocalTime.of(13, 0)));
        Assertions.assertThrows(Exception.class, () -> objectInTest.calculateHighestDiscountAmount(showing));
    }


    @Test
    void firstShowDiscountHighestWhenMultipleRulesValid() {
        Showing showing = createShowing(createMovie(true),1,LocalDateTime.of(LocalDate.of(2022,01,07), LocalTime.of(13, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,3.0);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void defaultToNoDiscountWhenNoRuleMatched() {
        Showing showing = createShowing(createMovie(false),10,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(10, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,0.0);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());

    }

    @Test
    void specialMovieDiscount() {
        Showing showing = createShowing(createMovie(true),10,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(9, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,2.0);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void firstShowDiscount() {
        Showing showing = createShowing(createMovie(false),1,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(9, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,3.0);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void secondShowDiscount() {
        Showing showing = createShowing(createMovie(false),2,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(9, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,1.0);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void afternoonShowDiscount() {
        Showing showing = createShowing(createMovie(false),5,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(13, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,2.5);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void afternoonShowAt11Discount() {
        Showing showing = createShowing(createMovie(false),5,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(11, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,2.5);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void afternoonShowAt4Discount() {
        Showing showing = createShowing(createMovie(false),5,LocalDateTime.of(LocalDate.of(2022,01,01), LocalTime.of(16, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,2.5);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }

    @Test
    void seventhDayOfMonthDiscount() {
        Showing showing = createShowing(createMovie(false),5,LocalDateTime.of(LocalDate.of(2022,01,07), LocalTime.of(10, 0)));
        double discount = objectInTest.calculateHighestDiscountAmount(showing);
        Assertions.assertEquals(discount,1.0);
        Assertions.assertTrue(discount < showing.getMovie().getTicketPrice());
    }


    private Showing createShowing(Movie movie, int sequence,LocalDateTime showTime) {
       return new Showing(movie,sequence, showTime);
    }
    private Movie createMovie(boolean isSpecial) {
        return  new Movie("Spider-Man: No Way Home", "Super hero movie",
                Duration.ofMinutes(90), 10, isSpecial);
    }
}