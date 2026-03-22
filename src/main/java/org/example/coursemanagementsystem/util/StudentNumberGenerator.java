package org.example.coursemanagementsystem.util;

import java.util.concurrent.ThreadLocalRandom;

public class StudentNumberGenerator {
    private StudentNumberGenerator() {}

    public static String generate() {
        int number = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.valueOf(number);
    }
}
