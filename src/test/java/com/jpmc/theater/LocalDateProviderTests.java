package com.jpmc.theater;

import com.jpmc.theater.processor.LocalDateProvider;
import org.junit.jupiter.api.Test;

public class LocalDateProviderTests {

    @Test
    void makeSureCurrentTime() {
        System.out.println("current time is - " + LocalDateProvider.singleton().currentDate());
    }

}
