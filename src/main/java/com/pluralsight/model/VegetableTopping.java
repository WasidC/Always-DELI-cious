package com.pluralsight.model;

/**
 * Regular vegetable toppings are included (price 0.0).
 */

public class VegetableTopping extends Topping {
    public VegetableTopping(String name) { super(name, false); }

    @Override
    public double priceFor(String sizeStr) { return 0.0; }
}
