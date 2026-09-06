import java.util.Scanner;

public class A9_Power {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter base: ");
        int base = scanner.nextInt();
        System.out.print("Enter exponent: ");
        int exponent = scanner.nextInt();
        
        long result = 1;
        int expTemp = exponent;
        
        while (expTemp != 0) {
            result *= base;
            --expTemp;
        }
        
        System.out.println(base + "^" + exponent + " = " + result);
        scanner.close();
    }
}
