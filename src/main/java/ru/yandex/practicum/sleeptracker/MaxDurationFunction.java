package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MaxDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>("Максимальная продолжительность сессии (мин)",
                sessions.stream()
                        .map(session -> Duration.between(session.getStartDateTime(), session.getEndDateTime()))
                        .max(Duration::compareTo)
                        .orElse(Duration.ZERO)
                        .toMinutes());
    }
}