package library.model;

public class DigitalResource extends LibraryResource implements Printable {
    public DigitalResource(int resourceId, String title, String author) {
        super(resourceId, title, author);
    }

    @Override
    public double calculateFine(int overdueDays) {
        return overdueDays * 2.0;
    }

    @Override
    public void printDetails() {
        System.out.println("DigitalResource [ID=" + getResourceId() + ", Title=" + getTitle() + ", Author=" + getAuthor() + "]");
    }
}
