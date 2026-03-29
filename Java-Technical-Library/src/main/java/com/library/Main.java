package com.library;

import com.library.repository.CategoryRepository;
import com.library.repository.ResourceRepository;
import com.library.service.DataPersistenceService;
import com.library.service.LibraryService;
import com.library.ui.ConsoleUI;
import com.library.util.SampleDataLoader;

import java.io.File;

/**
 * Entry point for the Technical Library & Resource Manager.
 *
 * Wiring order:
 *   Repositories → LibraryService → DataPersistenceService → ConsoleUI
 *
 * If no saved data exists, a curated set of 20 sample resources is loaded
 * so the application is immediately usable on first run.
 */
public class Main {

    public static void main(String[] args) {

        // 1. Create repositories
        ResourceRepository resourceRepo = new ResourceRepository();
        CategoryRepository categoryRepo  = new CategoryRepository();

        // 2. Wire service layer
        LibraryService libraryService = new LibraryService(resourceRepo, categoryRepo);
        DataPersistenceService persistenceService = new DataPersistenceService(resourceRepo);

        // 3. Load sample data on first run (before ConsoleUI loads saved file)
        boolean firstRun = !new File("data/library.csv").exists();
        if (firstRun) {
            System.out.println("  [INFO] First run detected — loading sample resources.");
            SampleDataLoader.load(libraryService);
        }

        // 4. Launch the console UI (it will also attempt to load the CSV)
        ConsoleUI ui = new ConsoleUI(libraryService, persistenceService);
        ui.start();
    }
}