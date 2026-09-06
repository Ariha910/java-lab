package food.service;

import food.model.FoodOrder;
import food.utility.OrderUtility;

public class OrderService {
    public void processOrder(FoodOrder order) {
        if (OrderUtility.validateAmount(order.getAmount()) && OrderUtility.validateCustomerName(order.getCustomerName())) {
            OrderUtility.generateOrderSummary(order);
        } else {
            System.out.println("Invalid order details for Order ID: " + order.getOrderId());
        }
    }
}
