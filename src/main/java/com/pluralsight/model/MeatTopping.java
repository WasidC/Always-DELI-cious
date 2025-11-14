package com.pluralsight.model;

/**
 * Meat topping pricing:
 * 4" -> 1.00, 8" -> 2.00, 12" -> 3.00
 * extra meat per extra: 0.50 / 1.00 / 1.50
 */

public class MeatTopping extends Topping {
    public MeatTopping(String name) { super(name, true); }

    @Override
    public double priceFor(String sizeStr) {
        return switch (sizeStr) {
            case "4" -> 1.00;
            case "8" -> 2.00;
            case "12" -> 3.00;
            default -> 1.00;
        };
    }
}
