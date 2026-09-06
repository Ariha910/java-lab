import java.util.Scanner;

public class A7_CountDigits {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int count = 0;
        long num = scanner.nextLong();
        
        long temp = num;
        if (temp == 0) {
            count = 1;
        } else {
            while (temp != 0) {
                temp /= 10;
                ++count;
            }
        }
        
        System.out.println("Number of digits in " + num + " is " + count);
        scanner.close();
    }
}
