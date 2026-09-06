import java.util.Scanner;

public class A8_ReverseNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        long num = scanner.nextLong();
        long reversed = 0;
        long temp = num;
        
        while(temp != 0) {
            long digit = temp % 10;
            reversed = reversed * 10 + digit;
            temp /= 10;
        }
        
        System.out.println("Reversed Number of " + num + " is " + reversed);
        scanner.close();
    }
}
