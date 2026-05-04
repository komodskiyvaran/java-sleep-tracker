package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {

    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Ваш хронотип", "голубь");
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
        if (firstStart.toLocalTime().isBefore(LocalTime.NOON)) {
            firstNight = firstNight.minusDays(1);
        } else {
            firstNight = firstNight.plusDays(1);
        }

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
            return new SleepAnalysisResult<>("Ваш хронотип", "голубь");
        }


        Map<String, Long> typeCount = java.util.stream.LongStream.rangeClosed(0, java.time.temporal.ChronoUnit.DAYS.between(firstNight, lastNight))
                .mapToObj(firstNight::plusDays)
                .filter(night -> isNightCovered(night, sessions))
                .map(night -> determineChronotypeForNight(night, sessions))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (typeCount.isEmpty()) {
            return new SleepAnalysisResult<>("Ваш хронотип", "голубь");
        }


        String result = typeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("голубь");


        long maxCount = typeCount.values().stream().max(Long::compareTo).orElse(0L);
        long tieCount = typeCount.values().stream().filter(count -> count == maxCount).count();
        if (tieCount > 1) {
            result = "голубь";
        }

        return new SleepAnalysisResult<>("Ваш хронотип", result);
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

    private String determineChronotypeForNight(LocalDate night, List<SleepingSession> sessions) {
        LocalDateTime nightStart = night.atTime(0, 0);
        LocalDateTime nightEnd = night.atTime(6, 0);

        SleepingSession coveringSession = sessions.stream()
                .filter(session -> {
                    LocalDateTime sStart = session.getStartDateTime();
                    LocalDateTime sEnd = session.getEndDateTime();
                    return sEnd.isAfter(nightStart) && sStart.isBefore(nightEnd);
                })
                .findFirst()
                .orElse(null);

        if (coveringSession == null) {
            return "голубь";
        }

        LocalTime sleepTime = coveringSession.getStartDateTime().toLocalTime();
        LocalTime wakeTime = coveringSession.getEndDateTime().toLocalTime();

        if (sleepTime.isAfter(LocalTime.of(23, 0)) && wakeTime.isAfter(LocalTime.of(9, 0))) {
            return "сова";
        }
        if (sleepTime.isBefore(LocalTime.of(22, 0)) && wakeTime.isBefore(LocalTime.of(7, 0))) {
            return "жаворонок";
        }
        return "голубь";
    }
}