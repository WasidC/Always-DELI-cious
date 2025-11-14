package com.pluralsight.model;

/**
 * Cheese topping pricing:
 * 4" -> 0.75, 8" -> 1.50, 12" -> 2.25
 * extra cheese per extra: 0.30 / 0.60 / 0.90
 */

public class CheeseTopping extends Topping {
    public CheeseTopping(String name) { super(name, true); }

    @Override
    public double priceFor(String sizeStr) {
        return switch (sizeStr) {
            case "4" -> 0.75;
            case "8" -> 1.50;
            case "12" -> 2.25;
            default -> 0.75;
        };
    }
}
