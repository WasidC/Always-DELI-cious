package com.pluralsight.model;

/**
 * Abstract product base class. Uses Strings for types.
 */

public abstract class Product {
    protected String name;
    protected double basePrice;

    public Product(String name, double basePrice) {
        this.name = (name == null || name.isBlank()) ? "Product" : name;
        this.basePrice = basePrice;
    }

    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }

    /**
     * Concrete classes must compute their price.
     * @return price in dollars
     */

    public abstract double getPrice();

    @Override
    public String toString() {
        return name + " ($" + String.format("%.2f", getPrice()) + ")";
    }
}
