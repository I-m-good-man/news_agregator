package VariousUtils;

import java.sql.Timestamp;

public class VariousUtils {
    public static Integer calculateTimeDelta(Integer limitPerDay) {
        // возвращает время в миллисекундах, через которое нужно делать запросы
        double secsInDay = 24 * 60 * 60;
        double k = 1.3;

        Integer time = Math.toIntExact((long) (secsInDay / limitPerDay * 1000 * k));
        return time;
    }

    public static Integer getCurrentTimestampSeconds() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Integer currentTimestampSeconds = Math.toIntExact(currentTimestamp.getTime() / 1000);
        return currentTimestampSeconds;
    }

//    public static void main(String[] args) {
//        System.out.println(VariousUtils.getCurrentTimestampSeconds());
//        System.out.println(VariousUtils.calculateTimeDelta(5000));
//    }
}
