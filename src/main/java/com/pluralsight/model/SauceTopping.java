package com.pluralsight.model;

/**
 * Sauces are regular toppings (included).
 */

public class SauceTopping extends Topping {
    public SauceTopping(String name) { super(name, false); }

    @Override
    public double priceFor(String sizeStr) { return 0.0; }
}
