package com.pluralsight.model;


public class Discount {
    private String code;
    private double amount;
    private String type; // "PERCENT" or "AMOUNT"
    private String description;

    public Discount(String code, double amount, String type, String description) {
        this.code = code.toLowerCase();
        this.amount = amount;
        this.type = type.toUpperCase();
        this.description = description;
    }

    public String getCode() { return code; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getDescription() { return description; }
}
