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

    @Override
    public String toString() {
        return "SleepingSession{" + "startDateTime=" + startDateTime.format(SleepTrackerApp.DATE_TIME_FORMATTER) + ", endDateTime=" + endDateTime.format(SleepTrackerApp.DATE_TIME_FORMATTER) + ", quality=" + quality + '}';
    }
}

enum Quality {
    GOOD, NORMAL, BAD
}