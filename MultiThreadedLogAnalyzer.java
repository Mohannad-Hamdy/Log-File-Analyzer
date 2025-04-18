import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;

public class MultiThreadedLogAnalyzer {
    private static final String FILE_PATH = "large_log.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int BURST_THRESHOLD = 5;
    private static final int MAX_BURST_INTERVAL = 2;
    private static final int THREAD_COUNT = 1;
//    Runtime.getRuntime().availableProcessors()
    static class AnalysisResult {
        Map<String, Integer> minuteTotal = new HashMap<>();
        Map<String, Integer> minuteErrors = new HashMap<>();
        int burstCount = 0;
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        System.out.println("Processing with multiple threads...");

        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<AnalysisResult>> futures = new ArrayList<>();

        
        int chunkSize = lines.size() / THREAD_COUNT;
        for (int i = 0; i < THREAD_COUNT; i++) {
            int start = i * chunkSize;
            int end = (i == THREAD_COUNT - 1) ? lines.size() : start + chunkSize;
            List<String> chunk = lines.subList(start, end);
            futures.add(executor.submit(() -> analyzeChunk(chunk)));
        }

        
        Map<String, Integer> totalMinuteTotal = new HashMap<>();
        Map<String, Integer> totalMinuteErrors = new HashMap<>();
        int totalBurstCount = 0;

        for (Future<AnalysisResult> future : futures) {
            AnalysisResult result = future.get();

            
            for (Map.Entry<String, Integer> entry : result.minuteTotal.entrySet()) {
                totalMinuteTotal.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }

            
            for (Map.Entry<String, Integer> entry : result.minuteErrors.entrySet()) {
                totalMinuteErrors.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }

            totalBurstCount += result.burstCount;
        }

        executor.shutdown();

        
        Map<String, Double> errorRates = new TreeMap<>();
        totalMinuteTotal.forEach((minute, total) -> {
            int errors = totalMinuteErrors.getOrDefault(minute, 0);
            errorRates.put(minute, (double) errors / total * 100);
        });

        long endTime = System.currentTimeMillis();

        
        System.out.println("\n=== Error Rates per Minute ===");
        errorRates.forEach((minute, rate) ->
                System.out.printf("%s: %.2f%%\n", minute, rate));

        System.out.println("\n=== Burst Detection ===");
        System.out.println("Total error bursts detected: " + totalBurstCount);
        System.out.println("\nProcessing time: " + (endTime - startTime) + "ms");
    }

    private static AnalysisResult analyzeChunk(List<String> lines) {
        AnalysisResult result = new AnalysisResult();

        LocalDateTime previousTimestamp = null;
        int consecutiveErrors = 0;

        for (String line : lines) {
            try {
                String[] parts = line.split(" ", 4);
                LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], TIMESTAMP_FORMATTER);
                String level = parts[2];
                String minuteKey = timestamp.truncatedTo(ChronoUnit.MINUTES).format(TIMESTAMP_FORMATTER);

                
                result.minuteTotal.put(minuteKey, result.minuteTotal.getOrDefault(minuteKey, 0) + 1);
                if ("ERROR".equals(level)) {
                    result.minuteErrors.put(minuteKey, result.minuteErrors.getOrDefault(minuteKey, 0) + 1);
                }

                
                if ("ERROR".equals(level)) {
                    if (previousTimestamp != null &&
                        Duration.between(previousTimestamp, timestamp).getSeconds() <= MAX_BURST_INTERVAL) {
                        consecutiveErrors++;
                        if (consecutiveErrors >= BURST_THRESHOLD) {
                            result.burstCount++;
                            consecutiveErrors = 0;
                        }
                    } else {
                        consecutiveErrors = 1;
                    }
                } else {
                    consecutiveErrors = 0;
                }
                previousTimestamp = timestamp;
            } catch (Exception e) {
                
            }
        }

        return result;
    }
}
