package food.utility;

import food.model.FoodOrder;
import food.model.Discountable;

public class OrderUtility {
    public static boolean validateAmount(double amount) {
        return amount > 0;
    }

    public static boolean validateCustomerName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static void generateOrderSummary(FoodOrder order) {
        System.out.println("-------------------------------------------------");
        System.out.println("Restaurant: " + FoodOrder.getRestaurantName());
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer Name: " + order.getCustomerName());
        System.out.println("Bill Amount: Rs. " + order.getAmount());
        
        double discount = 0;
        if (order instanceof Discountable) {
            discount = ((Discountable) order).applyDiscount();
        }
        System.out.println("Discount: Rs. " + discount);
        
        double deliveryCharge = order.calculateDeliveryCharge();
        System.out.println("Delivery Charge: Rs. " + deliveryCharge);
        
        double finalPayableAmount = order.getAmount() - discount + deliveryCharge;
        System.out.println("Final Payable Amount: Rs. " + finalPayableAmount);
        System.out.println("-------------------------------------------------");
    }
}
