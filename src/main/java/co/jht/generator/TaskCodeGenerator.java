package co.jht.generator;

import java.util.concurrent.atomic.AtomicLong;

public class TaskCodeGenerator {
    private static final String PREFIX = "JHT-";
    private static final AtomicLong COUNTER = new AtomicLong(10000);

    public static String generateTaskCode() {
        return PREFIX + COUNTER.getAndIncrement();
    }
}