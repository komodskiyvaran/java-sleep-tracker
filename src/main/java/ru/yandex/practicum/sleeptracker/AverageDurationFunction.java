package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>("Средняя продолжительность сессии (мин)",
                String.format("%.2f", sessions.stream()
                        .mapToLong(session -> Duration.between(
                                        session.getStartDateTime(),
                                        session.getEndDateTime())
                                .toMinutes())
                        .average()
                        .orElse(0.0)));
    }
}