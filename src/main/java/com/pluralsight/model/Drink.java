package com.pluralsight.model;

/**
 * Drink product. Size uses String values "SMALL","MEDIUM","LARGE".
 */

public class Drink extends Product {
    private final String size;

    public Drink(String name, String size) {
        super(name == null || name.isBlank() ? "Drink" : name, priceForSize(size));
        this.size = (size == null) ? "SMALL" : size.toUpperCase();
    }

    private static double priceForSize(String size) {
        if (size == null) return 2.0;
        return switch (size.toUpperCase()) {
            case "SMALL" -> 2.0;
            case "MEDIUM" -> 2.5;
            case "LARGE" -> 3.0;
            default -> 2.0;
        };
    }

    public String getSize() { return size; }

    @Override
    public double getPrice() { return basePrice; }
}
