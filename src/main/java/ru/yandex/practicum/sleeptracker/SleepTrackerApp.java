package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) throws IOException {
        List<SleepingSession> listSession = loadSessions(args[0]);

        List<Function<List<SleepingSession>, SleepAnalysisResult<?>>> functions = List.of(
                new CountSessionFunction(),
                new MinDurationFunction(),
                new MaxDurationFunction(),
                new AverageDurationFunction(),
                new CountBadSessionFunction(),
                new SleeplessNightsFunction(),
                new ChronotypeFunction());
        printFunction(functions, listSession);

    }

    private static List<SleepingSession> loadSessions(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .map(line -> line.split(";"))
                .map(parts -> new SleepingSession(
                        LocalDateTime.parse(parts[0], DATE_TIME_FORMATTER),
                        LocalDateTime.parse(parts[1], DATE_TIME_FORMATTER),
                        Quality.valueOf(parts[2])))
                .toList();
    }

    private static void printFunction(List<Function<List<SleepingSession>, SleepAnalysisResult<?>>> functions, List<SleepingSession> listSession) {
        functions.stream()
                .map(function -> function.apply(listSession))
                .forEach(result -> System.out.println(result.getDescription() + ": " + result.getValue()));
    }
}