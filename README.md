# Java Technical Library & Resource Manager

A console-based Java application to organize, track, and manage technical resources like research papers, books, articles, and tutorials. Built as a capstone project for the Programming in Java course.

---

## Why I Built This

As a student reviewing 20+ research papers and coding resources, I realized I had no proper system to track what I had read, what was pending, my personal notes, or ratings. Resources were scattered across browser bookmarks, notes apps, and random folders. This project solves that real problem by providing a single organized library where every resource can be added, searched, filtered, rated, and tracked.

---

## What This Project Does

- Add research papers, books, articles, tutorials, videos, and documentation
- Organize resources into categories like Machine Learning, Java, Distributed Systems
- Search resources by keyword across title, author, notes, and tags
- Filter by category, status, type, or tag
- Track reading status — Unread, Reading, Completed, Archived
- Rate resources from 1 to 5 stars
- Add personal notes to any resource
- View library statistics — total resources, completion rate, average rating
- Save everything to a CSV file so data persists between runs
- Comes preloaded with 20 real research papers on first run

---

## Project Structure

```
Java-Technical-Library/
│
├── src/main/java/com/library/
│   ├── Main.java                        Entry point
│   ├── ResourceRepositoryTest.java      Test class for repository
│   ├── LibraryServiceTest.java          Test class for service layer
│   │
│   ├── model/
│   │   ├── Resource.java                Core data model
│   │   └── Category.java               Category model
│   │
│   ├── repository/
│   │   ├── Repository.java              Generic repository interface
│   │   ├── ResourceRepository.java      Resource data access layer
│   │   └── CategoryRepository.java      Category data access layer
│   │
│   ├── service/
│   │   ├── LibraryService.java          Business logic layer
│   │   └── DataPersistenceService.java  CSV file read and write
│   │
│   ├── ui/
│   │   └── ConsoleUI.java              Interactive console menu
│   │
│   └── util/
│       ├── Printer.java                Terminal output formatting
│       └── SampleDataLoader.java       Preloads 20 sample resources
│
├── data/
│   └── library.csv                     Auto-generated data file
│
├── compile.sh                          Build script for Linux/Mac/Git Bash
├── run.sh                              Run script for Linux/Mac/Git Bash
└── test.sh                             Test runner for Linux/Mac/Git Bash
```

---

## Requirements

- Java JDK 17 or higher (project developed with JDK 25)
- No external libraries or dependencies required
- Works on Windows, Mac, and Linux

---

## How to Set Up and Run

### Option A — Using VS Code (Recommended for Windows)

1. Clone the repository
```
git clone https://github.com/YOUR_USERNAME/Java-Technical-Library.git
```

2. Open the folder in VS Code
```
File → Open Folder → select Java-Technical-Library
```

3. Open terminal in VS Code using Ctrl + backtick

4. Make sure you are in the root folder
```
PS C:\...\Java-Technical-Library>
```

5. Create output folder
```
mkdir out
```

6. Compile all files
```
javac -d out src/main/java/com/library/model/*.java src/main/java/com/library/repository/*.java src/main/java/com/library/service/*.java src/main/java/com/library/ui/*.java src/main/java/com/library/util/*.java src/main/java/com/library/*.java
```

7. Run the application
```
java -cp out com.library.Main
```

---

### Option B — Using Terminal on Linux or Mac

```bash
chmod +x compile.sh run.sh test.sh
./compile.sh
./run.sh
```

---

## How to Use the Application

When you run the app you will see the main menu:

```
  ╔══════════════════════════════════════════════════════╗
  ║       TECHNICAL LIBRARY & RESOURCE MANAGER          ║
  ╚══════════════════════════════════════════════════════╝

  ── MAIN MENU ──
  1. Add Resource
  2. View All Resources
  3. Search
  4. Filter Resources
  5. Resource Actions
  6. Manage Categories
  7. Statistics
  8. Save Library
  9. Save & Exit
```

Type the number and press Enter to select any option.

### Adding a Resource
Select option 1 and fill in the details when asked — title, author, type, category, URL, and tags.

### Searching
Select option 3 and type any keyword. The app searches across title, author, notes, category, and tags.

### Filtering
Select option 4 to filter by category, reading status, resource type, tag, or top rating.

### Resource Actions
Select option 5 to mark a resource as read, change its status, add personal notes, or delete it.

### Statistics
Select option 7 to see a full breakdown of your library — total resources, reading progress, category distribution, and average rating.

### Saving
Select option 8 to save anytime, or select option 9 to save and exit. Data is saved to data/library.csv automatically.

---

## How to Run Tests

```
java -cp out com.library.ResourceRepositoryTest
java -cp out com.library.LibraryServiceTest
```

All tests print PASS or FAIL for each case. No external testing framework is needed.

---

## Java Concepts Used

- Object Oriented Programming — classes, interfaces, inheritance, encapsulation
- Generics — generic Repository interface used by all repositories
- Collections — ArrayList, HashMap, LinkedHashMap used throughout
- Java Streams — filtering, searching, sorting, grouping with Stream API
- Enums — ResourceType and Status defined as enums inside Resource class
- File I/O — BufferedWriter and BufferedReader for CSV persistence
- Exception Handling — try catch blocks, custom error messages
- Optional — used in repository find methods to avoid null pointer exceptions
- Switch Expressions — modern Java switch used in ConsoleUI and Printer
- LocalDate — used for tracking date added and date read

---

## Sample Resources Preloaded

On first run the app loads 20 real technical resources including:

- Attention Is All You Need — Vaswani et al.
- MapReduce — Dean and Ghemawat
- Designing Data-Intensive Applications — Martin Kleppmann
- Java Concurrency in Practice — Goetz et al.
- Effective Java — Joshua Bloch
- Raft Consensus Algorithm — Ongaro and Ousterhout
- BERT — Devlin et al.
- Clean Code — Robert C. Martin
- and 12 more across Machine Learning, Distributed Systems, Algorithms, and Java

---

## Author

Shriya Patel
Programming in Java — Course Capstone Project
