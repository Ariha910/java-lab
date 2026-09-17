/**
 * Q2: Nested try-catch blocks to generate and handle
 * different exceptions in inner and outer blocks,
 * demonstrating exception propagation.
 */
public class NestedTryCatchDemo {

    public static void main(String[] args) {
        System.out.println("=== Nested Try-Catch Demonstration ===\n");

        // Scenario 1: Inner catches ArithmeticException, outer handles rest
        System.out.println("-- Scenario 1: Inner exception caught by inner block --");
        try {
            System.out.println("Outer try: start");
            try {
                System.out.println("  Inner try: attempting division by zero");
                int result = 10 / 0;
                System.out.println("  Result: " + result);
            } catch (ArithmeticException e) {
                System.out.println("  Inner catch: ArithmeticException -> " + e.getMessage());
            } finally {
                System.out.println("  Inner finally: always runs");
            }
            System.out.println("Outer try: continuing after inner block");
        } catch (Exception e) {
            System.out.println("Outer catch: " + e.getMessage());
        } finally {
            System.out.println("Outer finally: always runs\n");
        }

        // Scenario 2: Inner throws, outer catches (propagation)
        System.out.println("-- Scenario 2: Exception propagates to outer block --");
        try {
            System.out.println("Outer try: start");
            try {
                System.out.println("  Inner try: null pointer access");
                String s = null;
                s.length();               // throws NullPointerException
            } catch (ArithmeticException e) {
                // won't catch NPE
                System.out.println("  Inner catch: ArithmeticException (won't reach here)");
            } finally {
                System.out.println("  Inner finally: runs before propagation");
            }
        } catch (NullPointerException e) {
            System.out.println("Outer catch: NullPointerException caught -> " + e.getMessage());
        } finally {
            System.out.println("Outer finally: runs\n");
        }

        // Scenario 3: Triple nesting
        System.out.println("-- Scenario 3: Triple nested blocks --");
        try {
            try {
                try {
                    System.out.println("  Level 3 try: invalid array access");
                    int[] arr = new int[3];
                    arr[5] = 10;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("  Level 3 catch: " + e.getMessage());
                    throw new NumberFormatException("Re-thrown as NumberFormatException");
                }
            } catch (NumberFormatException e) {
                System.out.println("  Level 2 catch: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("  Level 1 catch: " + e.getMessage());
        }

        System.out.println("\n=== Nested try-catch demo complete ===");
    }
}
