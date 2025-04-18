import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class UnsafeLogAnalyzer {
    private static final String FILE_PATH = "large_log.txt";
    private static final int THREAD_POOL_SIZE = 4;
    static Map<String, Integer> countMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        System.out.println("Processing with UNSAFE shared counter...");
        
        long startTime = System.currentTimeMillis();
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int chunkSize = lines.size() / THREAD_POOL_SIZE;
        
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            int start = i * chunkSize;
            int end = (i == THREAD_POOL_SIZE - 1) ? lines.size() : start + chunkSize;
            executor.execute(new UnsafeTask(lines.subList(start, end)));
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Unsafe results: " + countMap);
        System.out.println("Unsafe processing took: " + (endTime - startTime) + "ms");
    }
}

class UnsafeTask implements Runnable {
    private final List<String> lines;
    
    public UnsafeTask(List<String> lines) {
        this.lines = lines;
    }
    
    @Override
    public void run() {
    for (String line : lines) {
        String level = line.split(" ")[2];
        
      
        for (int i = 0; i < 100; i++) {
            level = level.toLowerCase().toUpperCase();
        }
        
        
        int currentCount = UnsafeLogAnalyzer.countMap.getOrDefault(level, 0);
        UnsafeLogAnalyzer.countMap.put(level, currentCount + 1);
    }
}
}