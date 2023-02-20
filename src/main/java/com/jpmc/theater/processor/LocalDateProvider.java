package com.jpmc.theater.processor;

import java.io.Serializable;
import java.time.LocalDate;

public class LocalDateProvider implements Serializable {
    private static volatile LocalDateProvider instance = null;

    private LocalDateProvider () {
        if (instance != null) {
            throw new IllegalStateException("instance of LocalDateProvider is already available");
        }
    }

    /**
     * @return make sure to return singleton instance
     */
    public static LocalDateProvider singleton() {
        if (instance == null) {
            synchronized (LocalDateProvider.class) {
                if (instance == null)
                instance = new LocalDateProvider();
            }
        }
            return instance;
        }

    public LocalDate currentDate() {
            return LocalDate.now();
    }

    protected LocalDateProvider readResolve() {
        return singleton();
    }
}
