
# Log File Analyzer - Multi-threading Demonstration

A Java project showing the power of multi-threading and importance of synchronization when processing large files.

## 🎯 Project Idea
Analyze a 1-million entry log file to:
1. Compare processing speeds between single-threaded and multi-threaded approaches
2. Demonstrate data corruption without proper synchronization
3. Show correct implementation using locks

## 📄 Example Log File Structure
```plaintext
2023-01-01 21:43:28 WARN Message #130059
2023-01-01 21:43:28 ERROR Message #130060
2023-01-01 21:43:28 INFO Message #130061
2023-01-01 21:43:28 ERROR Message #130062
2023-01-01 21:43:28 ERROR Message #130063
2023-01-01 21:43:28 INFO Message #130064
...
```
- Contains 1,000,000 entries
- Random log levels (ERROR/WARN/INFO)
- Generated automatically by the program

## 🔍 Processing We Will Do
For each log entry:
1. Extract log level (ERROR/WARN/INFO)
2. Count occurrences of each log level
3. Compare results between different implementations
4. Track entries per minute and error entries per minute
5. Calculate error rate per minute
6. Detect bursts: 5+ consecutive ERRORs within 2 seconds of each other


## 📋 Key Scenarios Demonstrated
| Scenario | Speed | Data Accuracy | Learning Point |
|----------|-------|---------------|----------------|
| Single-threaded | Slow ✅ | Accurate | Baseline performance |
| Multi-threaded | Fast ✅ | Accurate | Proper threading benefits |
| Unsafe Threads | Fastest ✅ | Wrong counts | Sync needed for accuracy |
| Safe Threads |  < unsafe | Accurate | Correct sync implementation |

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

