package com.library.service;

import com.library.model.Resource;
import com.library.repository.ResourceRepository;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles saving and loading the library data to/from a CSV file.
 * CSV format (pipe-separated to avoid conflicts with commas in text):
 *
 * id|title|author|type|category|tags|url|notes|status|rating|dateAdded|dateRead
 *
 * This class intentionally avoids third-party libraries to stay within
 * the standard Java course curriculum.
 */
public class DataPersistenceService {

    private static final String DELIMITER = "|";
    private static final String DELIMITER_REGEX = "\\|";
    private static final String TAG_SEPARATOR = ";";
    private static final String DATA_FILE = "data/library.csv";
    private static final String HEADER =
            "id|title|author|type|category|tags|url|notes|status|rating|dateAdded|dateRead";

    private final ResourceRepository resourceRepo;

    public DataPersistenceService(ResourceRepository resourceRepo) {
        this.resourceRepo = resourceRepo;
    }

    /** Persist all resources in the repository to the CSV file. */
    public void saveToFile() throws IOException {
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            writer.write(HEADER);
            writer.newLine();
            for (Resource r : resourceRepo.findAll()) {
                writer.write(serialise(r));
                writer.newLine();
            }
        }
        System.out.println("  [OK] Library saved to " + DATA_FILE);
    }

    /** Load resources from the CSV file into the repository. */
    public void loadFromFile() throws IOException {
        Path path = Paths.get(DATA_FILE);
        if (!Files.exists(path)) {
            System.out.println("  [INFO] No saved data found. Starting fresh.");
            return;
        }

        List<String> lines = Files.readAllLines(path);
        int loaded = 0;
        for (int i = 1; i < lines.size(); i++) {          // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            try {
                Resource r = deserialise(line);
                resourceRepo.save(r);
                loaded++;
            } catch (Exception e) {
                System.err.println("  [WARN] Skipping malformed line " + (i + 1) + ": " + e.getMessage());
            }
        }
        System.out.println("  [OK] Loaded " + loaded + " resource(s) from " + DATA_FILE);
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private String serialise(Resource r) {
        return String.join(DELIMITER,
                safe(r.getId()),
                safe(r.getTitle()),
                safe(r.getAuthor()),
                safe(r.getType() != null ? r.getType().name() : ""),
                safe(r.getCategory()),
                safe(String.join(TAG_SEPARATOR, r.getTags())),
                safe(r.getUrl()),
                safe(r.getNotes() != null ? r.getNotes().replace("\n", "\\n") : ""),
                safe(r.getStatus().name()),
                String.valueOf(r.getRating()),
                safe(r.getDateAdded() != null ? r.getDateAdded().toString() : ""),
                safe(r.getDateRead() != null ? r.getDateRead().toString() : "")
        );
    }

    private Resource deserialise(String line) {
        String[] parts = line.split(DELIMITER_REGEX, -1);
        if (parts.length < 12) {
            throw new IllegalArgumentException("Expected 12 fields, got " + parts.length);
        }

        Resource r = new Resource();
        r.setId(parts[0].trim());
        r.setTitle(parts[1].trim());
        r.setAuthor(parts[2].trim());

        if (!parts[3].isBlank()) {
            r.setType(Resource.ResourceType.valueOf(parts[3].trim()));
        }
        r.setCategory(parts[4].trim().isEmpty() ? null : parts[4].trim());

        // tags
        if (!parts[5].isBlank()) {
            Arrays.stream(parts[5].split(TAG_SEPARATOR))
                  .filter(t -> !t.isBlank())
                  .forEach(r::addTag);
        }

        r.setUrl(parts[6].trim().isEmpty() ? null : parts[6].trim());
        String notes = parts[7].trim().replace("\\n", "\n");
        r.setNotes(notes.isEmpty() ? null : notes);

        if (!parts[8].isBlank()) {
            r.setStatus(Resource.Status.valueOf(parts[8].trim()));
        }

        if (!parts[9].isBlank()) {
            r.setRating(Integer.parseInt(parts[9].trim()));
        }
        if (!parts[10].isBlank()) {
            r.setDateAdded(LocalDate.parse(parts[10].trim()));
        }
        if (!parts[11].isBlank()) {
            r.setDateRead(LocalDate.parse(parts[11].trim()));
        }

        return r;
    }

    /** Replace null with empty string to avoid "null" in CSV. */
    private String safe(String value) {
        return value == null ? "" : value;
    }
}