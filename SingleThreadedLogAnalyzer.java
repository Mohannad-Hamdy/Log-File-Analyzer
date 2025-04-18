import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SingleThreadedLogAnalyzer {
    private static final String FILE_PATH = "large_log.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int BURST_THRESHOLD = 5; 
    private static final int MAX_BURST_INTERVAL = 2; 

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        System.out.println("Processing with single thread...");
        
        long startTime = System.currentTimeMillis();
        
        
        Map<String, Integer> minuteTotal = new HashMap<>(); 
        Map<String, Integer> minuteErrors = new HashMap<>(); 
        int burstCount = 0;
        
        
        LocalDateTime previousTimestamp = null;
        int consecutiveErrors = 0;

        for (String line : lines) {
            
            String[] parts = line.split(" ", 4);
            LocalDateTime timestamp = LocalDateTime.parse(
                parts[0] + " " + parts[1], TIMESTAMP_FORMATTER);
            String level = parts[2];
            String minuteKey = timestamp.truncatedTo(ChronoUnit.MINUTES)
                                      .format(TIMESTAMP_FORMATTER);

            
            minuteTotal.put(minuteKey, minuteTotal.getOrDefault(minuteKey, 0) + 1);
            if ("ERROR".equals(level)) {
                minuteErrors.put(minuteKey, minuteErrors.getOrDefault(minuteKey, 0) + 1);
            }

            
            if ("ERROR".equals(level)) {
                if (previousTimestamp != null && 
                    Duration.between(previousTimestamp, timestamp).getSeconds() <= MAX_BURST_INTERVAL) {
                    consecutiveErrors++;
                    if (consecutiveErrors >= BURST_THRESHOLD) {
                        burstCount++;
                        consecutiveErrors = 0; 
                    }
                } else {
                    consecutiveErrors = 1; 
                }
            } else {
                consecutiveErrors = 0; 
            }
            previousTimestamp = timestamp;
        }

        
        Map<String, Double> errorRates = new TreeMap<>();
        minuteTotal.forEach((minute, total) -> {
            int errors = minuteErrors.getOrDefault(minute, 0);
            errorRates.put(minute, (double) errors / total * 100);
        });

        long endTime = System.currentTimeMillis();

        
        System.out.println("\n=== Error Rates per Minute ===");
        errorRates.forEach((minute, rate) -> 
            System.out.printf("%s: %.2f%%\n", minute, rate));
        
        System.out.println("\n=== Burst Detection ===");
        System.out.println("Total error bursts detected: " + burstCount);
        System.out.println("\nProcessing time: " + (endTime - startTime) + "ms");
    }
}