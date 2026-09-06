package library.service;

import library.model.LibraryResource;
import library.model.Printable;
import library.util.InputValidator;

public class LibraryService {
    public void processResource(LibraryResource resource, int overdueDays) {
        if (!InputValidator.validateResourceId(resource.getResourceId())) {
            System.out.println("Invalid Resource ID.");
            return;
        }
        if (!InputValidator.validateFineDays(overdueDays)) {
            System.out.println("Invalid Overdue Days.");
            return;
        }

        if (resource instanceof Printable) {
            ((Printable) resource).printDetails();
        }
        
        double fine = resource.calculateFine(overdueDays);
        System.out.println("Overdue Days: " + overdueDays);
        System.out.println("Fine Amount: Rs. " + fine);
        System.out.println("-------------------------------------------------");
    }
}
