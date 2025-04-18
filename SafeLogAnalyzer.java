import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
public class SafeLogAnalyzer {
    private static final String FILE_PATH = "large_log.txt";
    private static final int THREAD_POOL_SIZE = 8;
    static Map<String, Integer> countMap = new HashMap<>();
    static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        System.out.println("Processing with SAFE locked counter...");
        
        long startTime = System.currentTimeMillis();
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int chunkSize = lines.size() / THREAD_POOL_SIZE;
        
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            int start = i * chunkSize;
            int end = (i == THREAD_POOL_SIZE - 1) ? lines.size() : start + chunkSize;
            executor.execute(new SafeTask(lines.subList(start, end)));
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Safe results: " + countMap);
        System.out.println("Safe processing took: " + (endTime - startTime) + "ms");
    }
}

class SafeTask implements Runnable {
    private final List<String> lines;
    
    public SafeTask(List<String> lines) {
        this.lines = lines;
    }
    
    @Override
    public void run() {
    for (String line : lines) {
        String level = line.split(" ")[2];
        
        
        for (int i = 0; i < 100; i++) {
            level = level.toLowerCase().toUpperCase();
        }
        
        
        SafeLogAnalyzer.lock.lock();
        try {
            int currentCount = SafeLogAnalyzer.countMap.getOrDefault(level, 0);
            SafeLogAnalyzer.countMap.put(level, currentCount + 1);
        } finally {
            SafeLogAnalyzer.lock.unlock();
        }
    }
}
}