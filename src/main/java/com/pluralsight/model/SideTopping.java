package com.pluralsight.model;

/**
 * Side items like au jus are treated as regular toppings (included).
 */

public class SideTopping extends Topping {
    public SideTopping(String name) { super(name, false); }

    @Override
    public double priceFor(String sizeStr) { return 0.0; }
}
