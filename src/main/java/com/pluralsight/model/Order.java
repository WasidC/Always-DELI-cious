package com.pluralsight.model;

import com.pluralsight.io.IPrintable;
import com.pluralsight.io.ISaveable;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Order holds products and implements printing and saving.
 */

public class Order implements IPrintable, ISaveable {

    private final List<Product> items = new ArrayList<>();
    private final LocalDateTime created = LocalDateTime.now();
    private Customer customer;
    private String appliedDiscountCode = null; // store code if applied
    private double discountAmount = 0.0; // computed discount amount on checkout

    public Order(Customer customer) {
        this.customer = (customer == null) ? new Customer("Guest", "") : customer;
    }

    public void addItem(Product p) {
        items.add(0, p); // newest first
    }

    public List<Product> getItems() { return List.copyOf(items); }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer c) { this.customer = c; }

    public LocalDateTime getCreated() { return created; }

    /**
     * Compute sum of product prices.
     */

    public double calculateTotal() {
        return items.stream().mapToDouble(Product::getPrice).sum();
    }

    public void setAppliedDiscount(String code, double amount) {
        this.appliedDiscountCode = code;
        this.discountAmount = amount;
    }

    public String getAppliedDiscountCode() { return appliedDiscountCode; }
    public double getDiscountAmount() { return discountAmount; }

    @Override
    public String printReceipt() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sb.append("DELI-cious Receipt\n");
        sb.append("Timestamp: ").append(created.format(f)).append("\n");
        sb.append("Customer: ").append(customer.toString()).append("\n");
        sb.append("-----------------------------\n");
        for (Product p : items) {
            sb.append(p.toString()).append("\n");
            sb.append("-----------------------------\n");
        }
        double subtotal = calculateTotal();
        sb.append(String.format("Subtotal: $%.2f%n", subtotal));
        if (appliedDiscountCode != null) {
            sb.append(String.format("Discount (%s): -$%.2f%n", appliedDiscountCode, discountAmount));
            sb.append(String.format("Total: $%.2f%n", Math.max(0.0, subtotal - discountAmount)));
        } else {
            sb.append(String.format("Total: $%.2f%n", subtotal));
        }
        return sb.toString();
    }

    @Override
    public Path saveToFile(String directory) throws Exception {
        return com.pluralsight.io.ReceiptGenerator.saveOrderReceipt(this, directory);
    }
}
