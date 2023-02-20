package com.jpmc.theater.model;

import static java.util.Collections.unmodifiableSet;
import com.jpmc.theater.processor.LocalDateProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Theater {
    private Set<Showing> showing;
    private LocalDateProvider dateProvider;

    public Theater(Set<Showing> showing,LocalDateProvider dateProvider) {
        this.showing = unmodifiableSet(showing);
        this.dateProvider = dateProvider;
    }

    public Theater(LocalDateProvider dateProvider) {
        this.dateProvider = dateProvider;
        showing = buildFixedSetOfShowing();
    }

    private Set<Showing> buildFixedSetOfShowing() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", "Super hero movie",Duration.ofMinutes(90), 12.5, true);
        Movie turningRed = new Movie("Turning Red", "Super hero movie. Not Suitable for kids",Duration.ofMinutes(85), 11, false);
        Movie theBatMan = new Movie("The Batman", "Super hero movie ",Duration.ofMinutes(95), 9, false);
        Set setOfShowing = new HashSet<Showing>();
        setOfShowing.addAll(List.of(
                new Showing(turningRed, 1, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(9, 0))),
                new Showing(spiderMan, 2, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(11, 0))),
                new Showing(theBatMan, 3, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(14, 30))),
                new Showing(spiderMan, 5, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(21, 10))),
                new Showing(theBatMan, 9, LocalDateTime.of(dateProvider.currentDate(), LocalTime.of(23, 0)))
        ));
        return setOfShowing;
    }

    public Set<Showing> getShowing() { return this.showing;}
}
