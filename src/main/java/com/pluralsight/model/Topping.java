package com.pluralsight.model;

/**
 * Abstract topping type. Subclasses implement size-specific pricing.
 * Size is passed as string: "4","8","12".
 */

public abstract class Topping {
    protected String name;
    protected boolean isPremium;

    public Topping(String name, boolean isPremium) {
        this.name = (name == null || name.isBlank()) ? "Topping" : name;
        this.isPremium = isPremium;
    }

    public String getName() { return name; }
    public boolean isPremium() { return isPremium; }

    /**
     * Price of the topping for a sandwich of size sizeStr (4/8/12).
     */

    public abstract double priceFor(String sizeStr);

    @Override
    public String toString() {
        return name + (isPremium ? " (premium)" : "");
    }
}
