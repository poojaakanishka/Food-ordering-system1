import java.util.Scanner;

public class FoodOrderingSystem {
    private static final String VALID_USERNAME = "user123";
    private static final String VALID_PASSWORD = "password123";

    public static boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***** LOGIN *****");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            System.out.println("Login successful. Welcome to the Food Ordering System!");
            return true;
        } else {
            System.out.println("Invalid username or password. Please try again.");
            return false;
        }
    }

    static MenuItem[] tasteUpMenu = {
        new MenuItem("Curd Rice", 40),
        new MenuItem("Dosa", 50),
        new MenuItem("Veg Briyani", 85),
        new MenuItem("Idiyappam", 45),
        new MenuItem("Vadai(3pcs)", 20)
    };
    static MenuItem[] foodzyMenu = {
        new MenuItem("Poori", 60),
        new MenuItem("Sweet Pongal", 70),
        new MenuItem("Chapati", 50),
        new MenuItem("Sambar Rice", 40),
        new MenuItem("Ghee Rice", 60)
    };

    static Restaurant[] restaurants = {
        new Restaurant("TASTE UP RESTAURANT", tasteUpMenu),
        new Restaurant("FOODZY RESTAURANT", foodzyMenu)
    };

    
    public static void displayRestaurantList() {
        System.out.println("Please choose a restaurant:");
        for (int i = 0; i < restaurants.length; i++) {
            System.out.println((i + 1) + ". " + restaurants[i].getName());
        }
    }

    public static void displayMenu(Restaurant restaurant) {
        System.out.println("WELCOME TO " + restaurant.getName() + "");
        System.out.println("--------------------------------");
        MenuItem[] menuItems = restaurant.getMenuItems();
        for (int i = 0; i < menuItems.length; i++) {
            System.out.println((i + 1) + ". " + menuItems[i].getName() + " Rs." + menuItems[i].getPrice());
        }
        System.out.println("--------------------------------");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            isAuthenticated = login();
        }

        boolean exit = false;
        int restaurantChoice;
        Order order = new Order(10);

        displayRestaurantList();
        System.out.print("Choose a restaurant by number: ");
        restaurantChoice = scanner.nextInt();
        Restaurant selectedRestaurant = restaurants[restaurantChoice - 1];
        displayMenu(selectedRestaurant);

        while (!exit) {
            System.out.print("Enter the number of the item you want to order (0 to finish): ");
            int itemChoice = scanner.nextInt();

            if (itemChoice == 0) {
                exit = true;
            } else {
                MenuItem selectedItem = selectedRestaurant.getMenuItem(itemChoice - 1);
                if (selectedItem != null) {
                    order.addItem(selectedItem);
                    System.out.println(selectedItem.getName() + " added to your order.");
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        if (order.getTotalAmount() > 0) {
            order.generateBill();

            if (Payment.onlinePayment(order.getTotalAmount())) {
                // Step 5: Delivery Management
                Delivery delivery = new Delivery();
                String address = delivery.collectDeliveryDetails();
                delivery.processDelivery(address);
                System.out.println("Order successfully placed!");
            }
        } else {
            System.out.println("No items ordered. Exiting system.");
        }
    }
}

class MenuItem {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class Restaurant {
    private String name;
    private MenuItem[] menuItems;

    public Restaurant(String name, MenuItem[] menuItems) {
        this.name = name;
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItem(int index) {
        if (index >= 0 && index < menuItems.length) {
            return menuItems[index];
        }
        return null;
    }
}

class Order {
    private MenuItem[] orderedItems;
    private int itemCount;
    private double totalAmount;

    public Order(int maxItems) {
        orderedItems = new MenuItem[maxItems];
        itemCount = 0;
        totalAmount = 0;
    }

    public void addItem(MenuItem item) {
        if (itemCount < orderedItems.length) {
            orderedItems[itemCount] = item;
            totalAmount += item.getPrice();
            itemCount++;
        }
    }

    public void generateBill() {
        System.out.println("\n***** Bill *****");
        for (int i = 0; i < itemCount; i++) {
            System.out.println(orderedItems[i].getName() + " - Rs." + orderedItems[i].getPrice());
        }
        System.out.println("Total amount: Rs." + totalAmount);
        System.out.println("");
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

class Payment {
    public static boolean onlinePayment(double total) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nOnline Payment Options:");
        System.out.println("1. Pay using QR Code");
        System.out.println("2. Pay using UPI");
        System.out.print("Choose your online payment method: ");
        int paymentChoice = scanner.nextInt();

        if (paymentChoice == 1 || paymentChoice == 2) {
            System.out.println("Processing online payment...");
            System.out.print("Enter the payment amount (Total: Rs." + total + "): ");
            double payment = scanner.nextDouble();
            if (payment >= total) {
                System.out.println("Payment successful! Change: Rs." + (payment - total));
                return true;
            } else {
                System.out.println("Insufficient payment. Try again.");
                return false;
            }
        } else {
            System.out.println("Invalid payment option.");
            return false;
        }
    }

    public static boolean offlinePayment(double total) {
        System.out.println("You have chosen cash on delivery.");
        System.out.println("Amount due on delivery: Rs." + total);
        return true;
    }
}

class Delivery {
    public String collectDeliveryDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter delivery address: ");
        return scanner.nextLine();
    }

    public void processDelivery(String address) {
        System.out.println("Processing your delivery...");
        System.out.println("Your order will be delivered to: " + address);
        System.out.println("Estimated delivery time: 30 minutes");
        System.out.println("Your order is on its way!");
    }
}
