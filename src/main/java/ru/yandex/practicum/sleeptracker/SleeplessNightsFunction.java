package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0L);
        }

        LocalDateTime firstStart = sessions.stream()
                .map(SleepingSession::getStartDateTime)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepingSession::getEndDateTime)
                .max(LocalDateTime::compareTo)
                .get();

        LocalDate firstNight = firstStart.toLocalDate();
        firstNight = firstStart.toLocalTime().isAfter(LocalTime.NOON) ? firstNight.plusDays(1) : firstNight;

        LocalDate lastNight = lastEnd.toLocalDate();
        if (lastEnd.toLocalTime().isBefore(LocalTime.of(6, 0))) {
            lastNight = lastNight.minusDays(1);
        }

        boolean hadNightSessionOnLastDay = sessions.stream()
                .anyMatch(session ->
                        session.getEndDateTime().toLocalDate().equals(lastEnd.toLocalDate()) &&
                                (session.getStartDateTime().toLocalTime().isBefore(LocalTime.of(6, 0)) ||
                                        session.getStartDateTime().toLocalDate().isBefore(session.getEndDateTime().toLocalDate()))
                );

        boolean lastSessionIsDaytime = sessions.stream()
                .filter(session -> session.getEndDateTime().equals(lastEnd))
                .allMatch(session ->
                        session.getStartDateTime().toLocalDate().equals(session.getEndDateTime().toLocalDate()));

        if (lastSessionIsDaytime && hadNightSessionOnLastDay) {
            lastNight = lastNight.plusDays(1);
        }

        if (lastNight.isBefore(firstNight)) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0L);
        }

        long sleeplessNights = LongStream.rangeClosed(0, ChronoUnit.DAYS.between(firstNight, lastNight))
                .mapToObj(firstNight::plusDays)
                .filter(night -> !isNightCovered(night, sessions))
                .count();

        return new SleepAnalysisResult<>("Количество бессонных ночей", sleeplessNights);
    }

    private boolean isNightCovered(LocalDate night, List<SleepingSession> sessions) {
        LocalDateTime nightStart = night.atTime(0, 0);
        LocalDateTime nightEnd = night.atTime(6, 0);

        return sessions.stream().anyMatch(session -> {
            LocalDateTime sStart = session.getStartDateTime();
            LocalDateTime sEnd = session.getEndDateTime();
            return sEnd.isAfter(nightStart) && sStart.isBefore(nightEnd);
        });
    }
}