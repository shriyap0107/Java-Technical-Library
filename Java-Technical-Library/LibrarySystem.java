import java.util.*;
import java.io.*;

class ResearchPaper implements Serializable {
    String title;
    String field;

    public ResearchPaper(String title, String field) {
        this.title = title;
        this.field = field;
    }

    @Override
    public String toString() {
        return "Title: " + title + " | Field: " + field;
    }
}

public class LibrarySystem {
    private static final String FILE_NAME = "library_data.txt";
    private static List<ResearchPaper> papers = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Technical Resource Manager ===");

        while (true) {
            System.out.println("\n1. Add Paper\n2. View All Papers\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter Paper Title: ");
                String title = scanner.nextLine();
                System.out.print("Enter Field (e.g., AI, ML, OS): ");
                String field = scanner.nextLine();
                papers.add(new ResearchPaper(title, field));
                System.out.println("Paper added successfully!");
            } else if (choice == 2) {
                System.out.println("\n--- Current Library ---");
                for (ResearchPaper p : papers) System.out.println(p);
            } else if (choice == 3) {
                System.out.println("Saving and exiting...");
                break;
            }
        }
    }
}
