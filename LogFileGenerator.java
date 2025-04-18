import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LogFileGenerator {
    private static final String[] LOG_LEVELS = {"ERROR", "WARN", "INFO"};
    private static final int FILE_SIZE = 1_000_000;
    private static final String FILE_PATH = "large_log.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        generateLogFile();
    }

    public static void generateLogFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            Random random = new Random();
            LocalDateTime timestamp = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
            
            for (int i = 0; i < FILE_SIZE; i++) {
                
                String level = LOG_LEVELS[random.nextInt(LOG_LEVELS.length)];
                String formattedTime = timestamp.format(TIMESTAMP_FORMATTER);
                            
                writer.write(formattedTime + " " + level + " Message #" + i + "\n"); 
                
                if (random.nextDouble() < 0.7) {
                    
                } else {
                    timestamp = timestamp.plusSeconds(1 + random.nextInt(3));
                }
            }
            
            System.out.println("Generated time-ordered log file with " + FILE_SIZE + " entries");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}