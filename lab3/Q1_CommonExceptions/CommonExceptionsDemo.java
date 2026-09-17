/**
 * Q1: Demonstrates hierarchy of common exceptions:
 * ArithmeticException, NullPointerException,
 * ArrayIndexOutOfBoundsException, NumberFormatException
 */
public class CommonExceptionsDemo {

    // Demonstrates ArithmeticException (division by zero)
    public static void demoArithmeticException() {
        System.out.println("\n--- ArithmeticException ---");
        try {
            int a = 10, b = 0;
            int result = a / b;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught ArithmeticException: " + e.getMessage());
        } finally {
            System.out.println("ArithmeticException block executed.");
        }
    }

    // Demonstrates NullPointerException
    public static void demoNullPointerException() {
        System.out.println("\n--- NullPointerException ---");
        try {
            String str = null;
            int length = str.length();   // throws NPE
            System.out.println("Length: " + length);
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException: " + e.getMessage());
        } finally {
            System.out.println("NullPointerException block executed.");
        }
    }

    // Demonstrates ArrayIndexOutOfBoundsException
    public static void demoArrayIndexOutOfBoundsException() {
        System.out.println("\n--- ArrayIndexOutOfBoundsException ---");
        try {
            int[] arr = {1, 2, 3};
            int value = arr[10];         // invalid index
            System.out.println("Value: " + value);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught ArrayIndexOutOfBoundsException: " + e.getMessage());
        } finally {
            System.out.println("ArrayIndexOutOfBoundsException block executed.");
        }
    }

    // Demonstrates NumberFormatException
    public static void demoNumberFormatException() {
        System.out.println("\n--- NumberFormatException ---");
        try {
            String invalidNumber = "abc123";
            int num = Integer.parseInt(invalidNumber);  // invalid parse
            System.out.println("Parsed: " + num);
        } catch (NumberFormatException e) {
            System.out.println("Caught NumberFormatException: " + e.getMessage());
        } finally {
            System.out.println("NumberFormatException block executed.");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Common Exceptions Demonstration ===");
        demoArithmeticException();
        demoNullPointerException();
        demoArrayIndexOutOfBoundsException();
        demoNumberFormatException();
        System.out.println("\n=== All exceptions handled successfully ===");
    }
}
