/**
 * Q3: Calculator that accepts two numbers and an operator (+, -, *, /).
 * Handles:
 *   - Division by zero (ArithmeticException)
 *   - Invalid numeric input (NumberFormatException)
 *   - Invalid operators (custom exception)
 */
public class Calculator {

    // Custom exception for invalid operator
    static class InvalidOperatorException extends Exception {
        public InvalidOperatorException(String operator) {
            super("Invalid operator: '" + operator + "'. Use +, -, *, /");
        }
    }

    /**
     * Performs the calculation and returns the result.
     */
    public static double calculate(String num1Str, String num2Str, String operator)
            throws InvalidOperatorException {

        double num1, num2;

        // Parse numbers — may throw NumberFormatException
        try {
            num1 = Double.parseDouble(num1Str);
            num2 = Double.parseDouble(num2Str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid numeric input: " + e.getMessage());
        }

        switch (operator) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero is not allowed");
                }
                return num1 / num2;
            default:
                throw new InvalidOperatorException(operator);
        }
    }

    /** Helper to run a test case and display result */
    private static void runTest(String a, String b, String op) {
        System.out.printf("  %s %s %s -> ", a, op, b);
        try {
            double result = calculate(a, b, op);
            System.out.printf("Result = %.2f%n", result);
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        } catch (InvalidOperatorException e) {
            System.out.println("InvalidOperatorException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Calculator with Exception Handling ===\n");

        System.out.println("Valid operations:");
        runTest("10", "5", "+");
        runTest("10", "5", "-");
        runTest("10", "5", "*");
        runTest("10", "5", "/");

        System.out.println("\nEdge cases:");
        runTest("10", "0", "/");        // division by zero
        runTest("10", "abc", "+");      // invalid number
        runTest("10", "5", "%");        // invalid operator

        System.out.println("\n=== Calculator demo complete ===");
    }
}
