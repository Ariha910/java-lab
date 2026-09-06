package library.main;

import library.model.LibraryResource;
import library.model.Book;
import library.model.DigitalResource;
import library.service.LibraryService;

public class Main {
    public static void main(String[] args) {
        LibraryResource[] resources = new LibraryResource[5];
        
        resources[0] = new Book(1, "Java Programming", "James Gosling");
        resources[1] = new DigitalResource(2, "Advanced Java E-Book", "Joshua Bloch");
        resources[2] = new Book(3, "Data Structures", "Robert Lafore");
        resources[3] = new DigitalResource(4, "Algorithms PDF", "Thomas Cormen");
        resources[4] = new Book(5, "Design Patterns", "Gang of Four");
        
        System.out.println("Library: " + LibraryResource.getLibraryName());
        System.out.println("Total Resources Created: " + LibraryResource.getTotalResources());
        System.out.println("=================================================");
        
        LibraryService service = new LibraryService();
        
        // Simulating overdue days for each resource
        int[] overdueDaysArray = { 5, 2, 0, 10, 3 };
        
        double totalFine = 0;
        for (int i = 0; i < resources.length; i++) {
            service.processResource(resources[i], overdueDaysArray[i]);
            totalFine += resources[i].calculateFine(overdueDaysArray[i]);
        }
        
        System.out.println("Total Fine for all overdue resources: Rs. " + totalFine);
    }
}
