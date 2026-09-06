package food.main;

import food.model.FoodOrder;
import food.model.RegularOrder;
import food.model.PremiumOrder;
import food.service.OrderService;

public class Main {
    public static void main(String[] args) {
        FoodOrder[] orders = new FoodOrder[6];
        
        orders[0] = new RegularOrder(101, "Alice", 500.0);
        orders[1] = new PremiumOrder(102, "Bob", 1200.0);
        orders[2] = new RegularOrder(103, "Charlie", 300.0);
        orders[3] = new PremiumOrder(104, "David", 800.0);
        orders[4] = new RegularOrder(105, "Eve", 450.0);
        orders[5] = new PremiumOrder(106, "Frank", 2000.0);
        
        System.out.println("Total Orders Created: " + FoodOrder.getTotalOrders());
        
        OrderService service = new OrderService();
        for (FoodOrder order : orders) {
            service.processOrder(order);
        }
    }
}
