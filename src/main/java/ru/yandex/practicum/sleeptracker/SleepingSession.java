package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Quality quality;

    public SleepingSession(LocalDateTime startDateTime, LocalDateTime endDateTime, Quality quality) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.quality = quality;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Quality getQuality() {
        return quality;
    }
}