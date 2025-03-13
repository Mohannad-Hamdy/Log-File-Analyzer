
# Log File Analyzer - Multi-threading Demonstration

A Java project showing the power of multi-threading and importance of synchronization when processing large files.

## 🎯 Project Idea
Analyze a 1-million entry log file to:
1. Compare processing speeds between single-threaded and multi-threaded approaches
2. Demonstrate data corruption without proper synchronization
3. Show correct implementation using locks

## 📄 Example Log File Structure
```plaintext
2023-01-01 ERROR Message #4231
2023-01-01 WARN Message #1894
2023-01-01 INFO Message #7562
...
```
- Contains 1,000,000 entries
- Random log levels (ERROR/WARN/INFO)
- Generated automatically by the program

## 🔍 Processing We Will Do
For each log entry:
1. Extract log level (ERROR/WARN/INFO)
2. Convert log level to lowercase and back to uppercase 100 times (simulate complex processing)
3. Count occurrences of each log level
4. Compare results between different implementations

## 📋 Key Scenarios Demonstrated
| Scenario | Speed | Data Accuracy | Learning Point |
|----------|-------|---------------|----------------|
| Single-threaded | Slow ✅ | Accurate | Baseline performance |
| Multi-threaded | Fast ✅ | Accurate | Proper threading benefits |
| Unsafe Threads | Fastest ❌ | Wrong counts | Sync needed for accuracy |
| Safe Threads | Fast ✅ | Accurate | Correct sync implementation |

## 🛠️ Project Development Steps
1. **Create Log Generator**:
   - Wrote `LogFileGenerator.java`
   - Generates 1M log entries with random levels

2. **Build Single-thread Version**:
   - Created `SingleThreadedLogAnalyzer.java`
   - Processes file sequentially

3. **Develop Multi-thread Version**:
   - Made `MultiThreadedLogAnalyzer.java`
   - Uses ExecutorService with 4 threads
   - Merges results from parallel processing

4. **Add Sync Demonstrations**:
   - Built `UnsafeLogAnalyzer.java` (no synchronization)
   - Created `SafeLogAnalyzer.java` (with ReentrantLock)
   - Added artificial processing complexity

5. **Testing & Validation**:
   - Compared execution times
   - Verified result accuracy
   - Tested with different thread counts

