package com.pluralsight;

import com.pluralsight.model.*;
import com.pluralsight.util.DiscountManager;
import com.pluralsight.util.MenuLoader;
import com.pluralsight.util.OrderHistory;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String RECEIPT_DIR = "receipts";
    private static final String HISTORY_DIR = "history";
    private static final String DATA_DIR = "data";

    // Managers
    private static final DiscountManager discountManager = new DiscountManager();
    private static final Menu menu = new Menu();
    private static OrderHistory orderHistory;

    public static void main(String[] args) throws Exception {
        // initialize
        System.out.println("Starting DELI-cious (Capstone 2) - Java 17 CLI");
        // load discounts (if any)
        discountManager.loadFromCsv(Path.of(DATA_DIR, "discounts.csv"));
        // load menu signatures
        MenuLoader.loadSignaturesFromCsv(menu, Path.of(DATA_DIR, "menu.csv"));
        orderHistory = new OrderHistory(Path.of(HISTORY_DIR));

        boolean running = true;
        while (running) {
            showMainMenu();
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> newOrderFlow();
                case "2" -> listOrderHistory();
                case "3" -> manageMenu();
                case "0" -> { running = false; System.out.println("Bye!"); }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n==== DELI-cious ====");
        System.out.println("1) New Order");
        System.out.println("2) Show Order History");
        System.out.println("3) Manage Menu (load CSV)");
        System.out.println("0) Exit");
        System.out.print("Choice: ");
    }

    private static void manageMenu() {
        System.out.println("Menu management");
        System.out.println("1) List signatures");
        System.out.println("2) Load menu.csv from data/");
        System.out.println("0) Back");
        System.out.print("Choice: ");
        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1" -> {
                if (menu.getSignatures().isEmpty()) System.out.println("No signature sandwiches available.");
                else {
                    int idx = 1;
                    for (SignatureSandwich s : menu.getSignatures()) {
                        System.out.println(idx++ + ") " + s);
                    }
                }
            }
            case "2" -> {
                MenuLoader.loadSignaturesFromCsv(menu, Path.of(DATA_DIR, "menu.csv"));
                System.out.println("Loaded menu.csv (if present).");
            }
            default -> {}
        }
    }

    private static void listOrderHistory() {
        System.out.println("Order history saved in " + HISTORY_DIR + " folder (history.txt and orders.csv)");
        System.out.println("Open the files to view past receipts.");
    }

    private static void newOrderFlow() {
        System.out.print("Customer name (blank -> Guest): ");
        String name = scanner.nextLine();
        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine();
        Customer customer = new Customer(name, phone);
        Order order = new Order(customer);

        boolean ordering = true;
        while (ordering) {
            System.out.println("\nOrder screen:");
            System.out.println("1) Add Sandwich");
            System.out.println("2) Add Drink");
            System.out.println("3) Add Chips");
            System.out.println("4) Apply Discount Code");
            System.out.println("5) View Order");
            System.out.println("6) Checkout");
            System.out.println("0) Cancel Order");
            System.out.print("Choice: ");
            String ch = scanner.nextLine().trim();
            switch (ch) {
                case "1" -> addSandwichInteractive(order);
                case "2" -> addDrinkInteractive(order);
                case "3" -> addChipsInteractive(order);
                case "4" -> applyDiscountInteractive(order);
                case "5" -> System.out.println(order.printReceipt());
                case "6" -> { checkout(order); ordering = false; }
                case "0" -> { System.out.println("Order cancelled."); ordering = false; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addSandwichInteractive(Order order) {
        System.out.println("Add Sandwich: choose signature or custom");
        System.out.println("1) Use signature sandwich");
        System.out.println("2) Build custom");
        System.out.print("Choice: ");
        String ch = scanner.nextLine().trim();
        Sandwich s;
        if ("1".equals(ch) && !menu.getSignatures().isEmpty()) {
            int idx = 1;
            for (SignatureSandwich sig : menu.getSignatures()) {
                System.out.println((idx++) + ") " + sig.getName() + " (" + sig.getSize() + "\" " + sig.getBreadType() + ")");
            }
            System.out.print("Choose number or 0 to cancel: ");
            try {
                int sel = Integer.parseInt(scanner.nextLine().trim());
                if (sel <= 0 || sel > menu.getSignatures().size()) {
                    System.out.println("Cancelled signature selection.");
                    return;
                }
                // clone chosen signature into a new sandwich for customization:
                SignatureSandwich template = menu.getSignatures().get(sel - 1);
                s = new SignatureSandwich(template.getName(), template.getSize(), template.getBreadType());
                // copy toppings
                for (Sandwich.ToppingCount tc : template.getToppings())
                    s.addTopping(tc.topping, tc.extraCount);
            } catch (Exception e) {
                System.out.println("Invalid input. Cancelled.");
                return;
            }
        } else {
            // build custom sandwich
            System.out.println("Choose size: 1) 4\" 2) 8\" 3) 12\"");
            String size = switch (scanner.nextLine().trim()) {
                case "1" -> "4";
                case "2" -> "8";
                default -> "12";
            };
            System.out.println("Choose bread: 1) WHITE 2) WHEAT 3) RYE 4) WRAP");
            String bread = switch (scanner.nextLine().trim()) {
                case "1" -> "WHITE";
                case "2" -> "WHEAT";
                case "3" -> "RYE";
                default -> "WRAP";
            };
            s = new Sandwich(size, bread);
        }

        System.out.print("Toasted? (y/n): ");
        s.setToasted(scanner.nextLine().trim().equalsIgnoreCase("y"));

        // toppings loop
        while (true) {
            System.out.println("Add topping: 1) Meat 2) Cheese 3) Vegetable 4) Sauce 5) Side 0) Done");
            System.out.print("Choice: ");
            String t = scanner.nextLine().trim();
            if ("0".equals(t)) break;
            System.out.print("Topping name: ");
            String tname = scanner.nextLine().trim();
            System.out.print("Extra count (0 for none): ");
            int extra = 0;
            try { extra = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception ignored) {}
            Topping top;
            switch (t) {
                case "1" -> top = new MeatTopping(tname);
                case "2" -> top = new CheeseTopping(tname);
                case "3" -> top = new VegetableTopping(tname);
                case "4" -> top = new SauceTopping(tname);
                case "5" -> top = new SideTopping(tname);
                default -> {
                    System.out.println("Unknown type, skipping.");
                    continue;
                }
            }
            s.addTopping(top, extra);
            System.out.println("Added topping: " + top.getName() + (extra>0 ? " (extra x"+extra+")" : ""));
        }
        order.addItem(s);
        System.out.println("Sandwich added.");
    }

    private static void addDrinkInteractive(Order order) {
        System.out.println("Add Drink:");
        System.out.println("1) Small  2) Medium  3) Large");
        String size = switch (scanner.nextLine().trim()) {
            case "1" -> "SMALL";
            case "2" -> "MEDIUM";
            default -> "LARGE";
        };
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        Drink d = new Drink(name.isBlank() ? "Drink" : name, size);
        order.addItem(d);
        System.out.println("Drink added: " + d);
    }

    private static void addChipsInteractive(Order order) {
        System.out.println("Add Chips (1) Regular 2) Salt & Vinegar - same price)");
        scanner.nextLine(); // just consume
        Chips c = new Chips();
        order.addItem(c);
        System.out.println("Chips added.");
    }

    private static void applyDiscountInteractive(Order order) {
        System.out.print("Enter discount code (or blank to cancel): ");
        String code = scanner.nextLine().trim();
        if (code.isBlank()) return;
        DiscountManager.Discount d = discountManager.getDiscount(code);
        if (d == null) {
            System.out.println("Invalid code.");
            return;
        }
        double subtotal = order.calculateTotal();
        double newTotal = discountManager.applyDiscount(subtotal, d);
        double discountAmt = Math.max(0.0, subtotal - newTotal);
        order.setAppliedDiscount(d.code, discountAmt);
        System.out.println("Applied discount: " + d.code + " -> saved $" + String.format("%.2f", discountAmt));
    }

    private static void checkout(Order order) {
        if (order.getItems().isEmpty()) {
            System.out.println("Order is empty. Add items before checkout.");
            return;
        }
        System.out.println("\n=== ORDER SUMMARY ===");
        System.out.println(order.printReceipt());
        System.out.print("Confirm and save? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("Checkout cancelled.");
            return;
        }
        try {
            Path p = order.saveToFile(RECEIPT_DIR);
            System.out.println("Saved receipt to: " + p);
            // record in history
            orderHistory.record(order);
            System.out.println("Order recorded to history.");
        } catch (Exception e) {
            System.out.println("Failed to save receipt: " + e.getMessage());
        }
    }
}
