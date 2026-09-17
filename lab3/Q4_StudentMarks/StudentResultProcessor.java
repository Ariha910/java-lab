/**
 * Q4: Student marks result-processing program.
 * - Marks must be between 0 and 100
 * - Custom InvalidMarksException thrown for invalid marks
 * - Calculates total, percentage, and grade only when all input is valid
 * - Displays a meaningful error message
 */
public class StudentResultProcessor {

    // ---- Custom Exception ----
    static class InvalidMarksException extends Exception {
        private final String subject;
        private final int marks;

        public InvalidMarksException(String subject, int marks) {
            super(String.format("Invalid marks for '%s': %d (must be between 0 and 100)", subject, marks));
            this.subject = subject;
            this.marks = marks;
        }

        public String getSubject() { return subject; }
        public int getMarks()     { return marks; }
    }

    // ---- Validation ----
    public static void validateMarks(String subject, int marks) throws InvalidMarksException {
        if (marks < 0 || marks > 100) {
            throw new InvalidMarksException(subject, marks);
        }
    }

    // ---- Grade Calculation ----
    public static String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }

    // ---- Processing ----
    public static void processStudentMarks(String name, int[] marks, String[] subjects) {
        System.out.println("\n--- Processing: " + name + " ---");
        try {
            // Validate all marks first
            for (int i = 0; i < subjects.length; i++) {
                validateMarks(subjects[i], marks[i]);
            }

            // All valid — compute result
            int total = 0;
            for (int m : marks) total += m;
            double percentage = (double) total / (subjects.length * 100) * 100;
            String grade = calculateGrade(percentage);

            System.out.println("  Subjects & Marks:");
            for (int i = 0; i < subjects.length; i++) {
                System.out.printf("    %-12s : %d%n", subjects[i], marks[i]);
            }
            System.out.printf("  Total      : %d/%d%n", total, subjects.length * 100);
            System.out.printf("  Percentage : %.2f%%%n", percentage);
            System.out.printf("  Grade      : %s%n", grade);

        } catch (InvalidMarksException e) {
            System.out.println("  ERROR - " + e.getMessage());
            System.out.println("  Result cannot be generated until all marks are valid.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Student Marks Result Processor ===");

        String[] subjects = {"Maths", "Physics", "Chemistry", "English", "CS"};

        // Valid student
        processStudentMarks("Alice", new int[]{92, 88, 75, 80, 95}, subjects);

        // Student with invalid marks (negative)
        processStudentMarks("Bob", new int[]{85, -10, 70, 90, 88}, subjects);

        // Student with marks > 100
        processStudentMarks("Charlie", new int[]{78, 110, 65, 70, 82}, subjects);

        // Failing student (valid marks)
        processStudentMarks("Diana", new int[]{40, 35, 42, 38, 30}, subjects);

        System.out.println("\n=== Processing complete ===");
    }
}
