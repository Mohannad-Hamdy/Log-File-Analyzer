# Log File Analyzer - Multi-threading Demonstration

A Java project showing the power of multi-threading and importance of synchronization when processing large files.

## 🎯 Project Idea
Analyze a 1-million entry log file to:
1. Compare processing speeds between single-threaded and multi-threaded approaches
2. Demonstrate data corruption without proper synchronization
3. Show correct implementation using locks

## 📋 Key Scenarios Demonstrated
| Scenario | Speed | Data Accuracy | Learning Point |
|----------|-------|---------------|----------------|
| Single-threaded | Slow ✅ | Accurate | Baseline performance |
| Multi-threaded | Fast ✅ | Accurate | Proper threading benefits |
| Unsafe Threads | Fastest ❌ | Wrong counts | Sync needed for accuracy |
| Safe Threads | Fast ✅ | Accurate | Correct sync implementation |

## 🛠️ Setup
1. **Requirements**:
   - Java 8+
   - Any IDE (NetBeans/Eclipse/IntelliJ)

2. **Clone & Run**:
```bash
