import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Q7: Shopping Cart Application with complete exception hierarchy:
 *
 *   ApplicationException
 *   ├── ProductException
 *   │   ├── ProductNotFoundException
 *   │   └── OutOfStockException
 *   ├── PaymentException
 *   │   ├── InvalidPaymentException
 *   │   └── InsufficientFundsException
 *   └── OrderException
 *       └── EmptyCartException
 *
 * Operations: add/remove items, payment, search products, etc.
 */
public class ShoppingCartApp {

    // =========================================================
    //  Exception Hierarchy
    // =========================================================

    /** Root application exception */
    static class ApplicationException extends Exception {
        public ApplicationException(String message) { super(message); }
        public ApplicationException(String message, Throwable cause) { super(message, cause); }
    }

    // -- Product exceptions --
    static class ProductException extends ApplicationException {
        public ProductException(String message) { super(message); }
    }

    static class ProductNotFoundException extends ProductException {
        public ProductNotFoundException(String productId) {
            super("Product not found: '" + productId + "'");
        }
    }

    static class OutOfStockException extends ProductException {
        private final String productName;
        private final int available;
        private final int requested;

        public OutOfStockException(String productName, int available, int requested) {
            super(String.format("'%s' is out of stock. Requested: %d, Available: %d",
                                productName, requested, available));
            this.productName = productName;
            this.available   = available;
            this.requested   = requested;
        }
        public int getAvailable() { return available; }
        public int getRequested() { return requested; }
    }

    // -- Payment exceptions --
    static class PaymentException extends ApplicationException {
        public PaymentException(String message) { super(message); }
    }

    static class InvalidPaymentException extends PaymentException {
        public InvalidPaymentException(String reason) {
            super("Invalid payment: " + reason);
        }
    }

    static class InsufficientFundsException extends PaymentException {
        private final double required;
        private final double available;

        public InsufficientFundsException(double required, double available) {
            super(String.format("Insufficient funds. Required: %.2f, Available: %.2f",
                                required, available));
            this.required  = required;
            this.available = available;
        }
        public double getRequired()  { return required; }
        public double getAvailable() { return available; }
    }

    // -- Order exceptions --
    static class OrderException extends ApplicationException {
        public OrderException(String message) { super(message); }
    }

    static class EmptyCartException extends OrderException {
        public EmptyCartException() {
            super("Cannot place order: shopping cart is empty.");
        }
    }

    // =========================================================
    //  Domain Model
    // =========================================================

    static class Product {
        private final String id;
        private final String name;
        private final double price;
        private int stock;

        public Product(String id, String name, double price, int stock) {
            this.id    = id;
            this.name  = name;
            this.price = price;
            this.stock = stock;
        }
        public String getId()    { return id;    }
        public String getName()  { return name;  }
        public double getPrice() { return price; }
        public int    getStock() { return stock; }
        public void   reduceStock(int qty) { stock -= qty; }

        @Override
        public String toString() {
            return String.format("[%s] %-18s $%.2f  (stock: %d)", id, name, price, stock);
        }
    }

    static class CartItem {
        private final Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product  = product;
            this.quantity = quantity;
        }
        public Product getProduct()  { return product;  }
        public int     getQuantity() { return quantity; }
        public void    addQty(int q) { quantity += q;   }
        public double  subtotal()    { return product.getPrice() * quantity; }
    }

    // =========================================================
    //  Product Catalog
    // =========================================================

    static class ProductCatalog {
        private final Map<String, Product> products = new HashMap<>();

        public void addProduct(Product p) { products.put(p.getId(), p); }

        public Product findById(String id) throws ProductNotFoundException {
            Product p = products.get(id);
            if (p == null) throw new ProductNotFoundException(id);
            return p;
        }

        public List<Product> searchByName(String keyword) {
            List<Product> results = new ArrayList<>();
            String lower = keyword.toLowerCase();
            for (Product p : products.values()) {
                if (p.getName().toLowerCase().contains(lower)) results.add(p);
            }
            return results;
        }

        public void displayAll() {
            System.out.println("  ---- Product Catalog ----");
            for (Product p : products.values()) System.out.println("  " + p);
        }
    }

    // =========================================================
    //  Shopping Cart
    // =========================================================

    static class ShoppingCart {
        private final Map<String, CartItem> items = new HashMap<>();

        /** Add qty of product to cart; validates stock availability */
        public void addItem(Product product, int qty) throws OutOfStockException {
            if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive.");
            if (product.getStock() < qty) {
                throw new OutOfStockException(product.getName(), product.getStock(), qty);
            }
            if (items.containsKey(product.getId())) {
                CartItem existing = items.get(product.getId());
                int totalQty = existing.getQuantity() + qty;
                if (product.getStock() < totalQty) {
                    throw new OutOfStockException(product.getName(), product.getStock(), totalQty);
                }
                existing.addQty(qty);
            } else {
                items.put(product.getId(), new CartItem(product, qty));
            }
            System.out.printf("  [CART ADD] %dx %s added to cart.%n", qty, product.getName());
        }

        /** Remove product from cart */
        public void removeItem(String productId) throws ProductNotFoundException {
            if (!items.containsKey(productId)) throw new ProductNotFoundException(productId);
            CartItem removed = items.remove(productId);
            System.out.printf("  [CART REMOVE] %s removed from cart.%n",
                              removed.getProduct().getName());
        }

        public boolean isEmpty() { return items.isEmpty(); }

        public double getTotal() {
            return items.values().stream().mapToDouble(CartItem::subtotal).sum();
        }

        public void displayCart() {
            System.out.println("  ---- Cart Contents ----");
            if (items.isEmpty()) { System.out.println("  (empty)"); return; }
            for (CartItem ci : items.values()) {
                System.out.printf("  %-20s x%d  =  $%.2f%n",
                    ci.getProduct().getName(), ci.getQuantity(), ci.subtotal());
            }
            System.out.printf("  Total: $%.2f%n", getTotal());
        }

        public Map<String, CartItem> getItems() { return items; }
    }

    // =========================================================
    //  Payment Service
    // =========================================================

    static class PaymentService {
        /**
         * Process payment.
         * @throws InvalidPaymentException  if method is null/invalid
         * @throws InsufficientFundsException if amount paid is less than total
         */
        public void processPayment(String method, double amountPaid, double totalRequired)
                throws InvalidPaymentException, InsufficientFundsException {

            if (method == null || method.trim().isEmpty()) {
                throw new InvalidPaymentException("Payment method cannot be empty.");
            }
            if (!method.equalsIgnoreCase("CARD") && !method.equalsIgnoreCase("CASH")
                && !method.equalsIgnoreCase("UPI")) {
                throw new InvalidPaymentException("Unsupported method '" + method
                    + "'. Use CARD, CASH, or UPI.");
            }
            if (amountPaid < totalRequired) {
                throw new InsufficientFundsException(totalRequired, amountPaid);
            }
            System.out.printf("  [PAYMENT SUCCESS] %.2f paid via %s. Change: %.2f%n",
                amountPaid, method.toUpperCase(), amountPaid - totalRequired);
        }
    }

    // =========================================================
    //  Order Service
    // =========================================================

    static class OrderService {
        private final PaymentService paymentService = new PaymentService();

        /**
         * Place order from cart.
         * @throws EmptyCartException         if cart is empty
         * @throws InvalidPaymentException    if payment method invalid
         * @throws InsufficientFundsException if insufficient funds
         */
        public void placeOrder(ShoppingCart cart, String paymentMethod, double amountPaid)
                throws EmptyCartException, InvalidPaymentException, InsufficientFundsException {

            if (cart.isEmpty()) throw new EmptyCartException();

            double total = cart.getTotal();
            System.out.printf("  [ORDER] Placing order. Total: $%.2f%n", total);

            paymentService.processPayment(paymentMethod, amountPaid, total);

            // Deduct stock
            for (CartItem ci : cart.getItems().values()) {
                ci.getProduct().reduceStock(ci.getQuantity());
            }
            System.out.println("  [ORDER] Order placed successfully! Stock updated.");
        }
    }

    // =========================================================
    //  Main — Demonstration
    // =========================================================

    public static void main(String[] args) {
        System.out.println("=== Shopping Cart Application ===\n");

        // Setup
        ProductCatalog catalog = new ProductCatalog();
        catalog.addProduct(new Product("P001", "Wireless Headphones", 59.99, 5));
        catalog.addProduct(new Product("P002", "Mechanical Keyboard",  89.99, 2));
        catalog.addProduct(new Product("P003", "USB-C Hub",            29.99, 0)); // out of stock
        catalog.addProduct(new Product("P004", "Laptop Stand",         39.99, 3));

        ShoppingCart cart    = new ShoppingCart();
        OrderService orders  = new OrderService();

        // --- Display catalog ---
        System.out.println("Available Products:");
        catalog.displayAll();

        // --- Search ---
        System.out.println("\nSearch for 'key':");
        List<Product> found = catalog.searchByName("key");
        found.forEach(p -> System.out.println("  Found: " + p));

        // --- Add items to cart ---
        System.out.println("\nAdding to cart:");
        safeAdd(catalog, cart, "P001", 2);   // valid
        safeAdd(catalog, cart, "P002", 1);   // valid
        safeAdd(catalog, cart, "P003", 1);   // OutOfStockException
        safeAdd(catalog, cart, "P999", 1);   // ProductNotFoundException

        cart.displayCart();

        // --- Remove item ---
        System.out.println("\nRemoving P002:");
        safeRemove(cart, "P002");

        // --- Place order with various payment scenarios ---
        System.out.println("\nPlacing order (insufficient funds):");
        safeOrder(orders, cart, "CARD", 50.00);   // insufficient

        System.out.println("\nPlacing order (invalid payment method):");
        safeOrder(orders, cart, "BITCOIN", 200.00);

        System.out.println("\nPlacing order (valid):");
        safeOrder(orders, cart, "UPI", 200.00);   // success

        // --- Empty cart order ---
        System.out.println("\nPlacing order on empty cart:");
        safeOrder(orders, new ShoppingCart(), "CARD", 100.00); // EmptyCartException

        // --- Final stock status ---
        System.out.println("\nUpdated Catalog:");
        catalog.displayAll();

        System.out.println("\n=== Shopping cart demo complete ===");
    }

    // ---- Helpers ----
    private static void safeAdd(ProductCatalog catalog, ShoppingCart cart,
                                 String productId, int qty) {
        try {
            Product p = catalog.findById(productId);
            cart.addItem(p, qty);
        } catch (ProductNotFoundException e) {
            System.out.println("  [ERROR] ProductNotFoundException: " + e.getMessage());
        } catch (OutOfStockException e) {
            System.out.println("  [ERROR] OutOfStockException: " + e.getMessage());
        }
    }

    private static void safeRemove(ShoppingCart cart, String productId) {
        try {
            cart.removeItem(productId);
        } catch (ProductNotFoundException e) {
            System.out.println("  [ERROR] ProductNotFoundException: " + e.getMessage());
        }
    }

    private static void safeOrder(OrderService orders, ShoppingCart cart,
                                   String method, double paid) {
        try {
            orders.placeOrder(cart, method, paid);
        } catch (EmptyCartException e) {
            System.out.println("  [ERROR] EmptyCartException: " + e.getMessage());
        } catch (InvalidPaymentException e) {
            System.out.println("  [ERROR] InvalidPaymentException: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("  [ERROR] InsufficientFundsException: " + e.getMessage());
        }
    }
}
