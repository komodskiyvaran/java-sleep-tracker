package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.Quality.*;
import static ru.yandex.practicum.sleeptracker.SleepTrackerApp.DATE_TIME_FORMATTER;

public class SleepTrackerAppTest {
    private static List<SleepingSession> sessions;
    private static List<SleepingSession> emptySessions;

    @BeforeAll
    static void setUp() {
        sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("05.10.25 13:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("05.10.25 14:15", DATE_TIME_FORMATTER),
                        BAD),
                new SleepingSession(
                        LocalDateTime.parse("10.10.25 10:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("11.10.25 11:35", DATE_TIME_FORMATTER),
                        GOOD)
        );

        emptySessions = List.of();
    }

    @Test
    void count_Sessions() {
        CountSessionFunction function = new CountSessionFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(2L, result.getValue());
    }

    @Test
    void count_Sessions_EmptyList() {
        CountSessionFunction function = new CountSessionFunction();
        SleepAnalysisResult<?> result = function.apply(emptySessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void minDuration_Sessions() {
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(45L, result.getValue());
    }

    @Test
    void minDuration_Sessions_EmptyList() {
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult<?> result = function.apply(emptySessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void maxDuration_Sessions() {
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(1505L, result.getValue());
    }

    @Test
    void maxDuration_Sessions_EmptyList() {
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult<?> result = function.apply(emptySessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void averageDuration_Sessions() {
        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals("775.00", result.getValue());
    }

    @Test
    void averageDuration_Sessions_EmptyList() {
        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult<?> result = function.apply(emptySessions);
        assertEquals("0.00", result.getValue());
    }

    @Test
    void countBad_Sessions() {
        List<SleepingSession> sessionsBad = List.of(
                new SleepingSession(
                        LocalDateTime.parse("05.10.25 13:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("05.10.25 14:15", DATE_TIME_FORMATTER),
                        BAD),
                new SleepingSession(
                        LocalDateTime.parse("10.10.25 10:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("11.10.25 11:35", DATE_TIME_FORMATTER),
                        GOOD),
                new SleepingSession(
                        LocalDateTime.parse("05.10.25 13:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("05.10.25 14:15", DATE_TIME_FORMATTER),
                        BAD),
                new SleepingSession(
                        LocalDateTime.parse("05.10.25 13:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("05.10.25 14:15", DATE_TIME_FORMATTER),
                        GOOD),
                new SleepingSession(
                        LocalDateTime.parse("05.10.25 13:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("05.10.25 14:15", DATE_TIME_FORMATTER),
                        BAD)
        );
        CountBadSessionFunction function = new CountBadSessionFunction();
        SleepAnalysisResult<?> result = function.apply(sessionsBad);
        assertEquals(3L, result.getValue());
    }

    @Test
    void countBad_Sessions_EmptyList() {
        CountBadSessionFunction function = new CountBadSessionFunction();
        SleepAnalysisResult<?> result = function.apply(emptySessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void sleeplessNights_oneSleeplessNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("01.10.25 23:15", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("02.10.25 07:30", DATE_TIME_FORMATTER),
                        GOOD),
                new SleepingSession(
                        LocalDateTime.parse("03.10.25 14:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("03.10.25 15:00", DATE_TIME_FORMATTER),
                        NORMAL)
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        assertEquals(1L, function.apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_zeroSleeplessNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("06.10.25 22:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("07.10.25 05:50", DATE_TIME_FORMATTER),
                        GOOD),
                new SleepingSession(
                        LocalDateTime.parse("07.10.25 23:45", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("08.10.25 06:30", DATE_TIME_FORMATTER),
                        NORMAL)
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        assertEquals(0L, function.apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_sessionCrossesMidnight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("08.10.25 23:50", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("09.10.25 07:10", DATE_TIME_FORMATTER),
                        GOOD),
                new SleepingSession(
                        LocalDateTime.parse("09.10.25 13:00", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("09.10.25 14:30", DATE_TIME_FORMATTER),
                        NORMAL)
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        assertEquals(1L, function.apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_firstSessionAfterNoon() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("03.10.25 14:10", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("03.10.25 15:00", DATE_TIME_FORMATTER),
                        GOOD),
                new SleepingSession(
                        LocalDateTime.parse("03.10.25 23:40", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("04.10.25 08:00", DATE_TIME_FORMATTER),
                        NORMAL)
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        assertEquals(0L, function.apply(sessions).getValue());
    }

    @Test
    void chronotype_Owl() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("01.10.25 23:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("02.10.25 09:30", DATE_TIME_FORMATTER),
                        GOOD
                )
        );
        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals("сова", result.getValue());
    }

    @Test
    void chronotype_Lark() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("01.10.25 21:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("02.10.25 06:30", DATE_TIME_FORMATTER),
                        GOOD
                )
        );
        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals("жаворонок", result.getValue());
    }

    @Test
    void chronotype_Pigeon() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("01.10.25 22:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("02.10.25 08:00", DATE_TIME_FORMATTER),
                        GOOD
                )
        );
        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals("голубь", result.getValue());
    }

    @Test
    void chronotype_TieBreaker_Pigeon() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.parse("01.10.25 23:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("02.10.25 09:30", DATE_TIME_FORMATTER),
                        GOOD
                ),
                new SleepingSession(
                        LocalDateTime.parse("02.10.25 21:30", DATE_TIME_FORMATTER),
                        LocalDateTime.parse("03.10.25 06:30", DATE_TIME_FORMATTER),
                        GOOD
                )
        );
        ChronotypeFunction function = new ChronotypeFunction();
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals("голубь", result.getValue());
    }
}