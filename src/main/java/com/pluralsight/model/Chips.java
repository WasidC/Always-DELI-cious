package com.pluralsight.model;

/**
 * Chips fixed-price product.
 */

public class Chips extends Product {

    public Chips() {
        super("Chips", 1.50);
    }

    @Override
    public double getPrice() { return basePrice; }
}