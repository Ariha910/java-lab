package library.util;

public class InputValidator {
    public static boolean validateResourceId(int id) {
        return id > 0;
    }

    public static boolean validateFineDays(int days) {
        return days >= 0;
    }
}
