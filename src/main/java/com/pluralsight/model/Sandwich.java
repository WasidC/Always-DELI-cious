package com.pluralsight.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Sandwich product composed of a bread base and toppings.
 * Uses Strings for size ("4","8","12") and bread type ("WHITE","WHEAT","RYE","WRAP").
 */

public class Sandwich extends Product {

    protected String size;
    protected String breadType;
    protected final List<ToppingCount> toppings = new ArrayList<>();
    protected boolean toasted = false;

    // bread base prices
    private static double breadPriceForSize(String s) {
        return switch (s) {
            case "4" -> 5.50;
            case "8" -> 7.00;
            case "12" -> 8.50;
            default -> 7.00;
        };
    }

    public Sandwich(String size, String breadType) {
        super("Custom Sandwich", breadPriceForSize(size));
        this.size = (size == null) ? "8" : size;
        this.breadType = (breadType == null || breadType.isBlank()) ? "WHITE" : breadType.toUpperCase();
    }

    public String getSize() { return size; }
    public String getBreadType() { return breadType; }
    public boolean isToasted() { return toasted; }
    public void setToasted(boolean t) { toasted = t; }

    /**
     * Add topping to the sandwich.
     * @param topping topping instance
     * @param extraCount number of extra units beyond included (0 if none)
     */

    public void addTopping(Topping topping, int extraCount) {
        toppings.add(new ToppingCount(topping, Math.max(0, extraCount)));
    }

    public List<ToppingCount> getToppings() {
        return List.copyOf(toppings);
    }

    /**
     * Calculate sandwich price including toppings. Premium toppings include their base
     * cost and extras are charged per size. Regular toppings cost 0 even for extras.
     */

    @Override
    public double getPrice() {
        double price = basePrice;
        for (ToppingCount tc : toppings) {
            Topping t = tc.topping;
            int extra = tc.extraCount;
            double baseT = t.priceFor(size);
            price += baseT;
            if (extra > 0 && t.isPremium()) {
                if (t instanceof MeatTopping) {
                    price += extra * extraMeatCost(size);
                } else if (t instanceof CheeseTopping) {
                    price += extra * extraCheeseCost(size);
                } else {
                    // default premium extra cost
                    price += extra * extraCheeseCost(size);
                }
            }
            // regular topping extras included
        }
        return price;
    }

    private static double extraMeatCost(String size) {
        return switch (size) {
            case "4" -> 0.50;
            case "8" -> 1.00;
            case "12" -> 1.50;
            default -> 1.00;
        };
    }

    private static double extraCheeseCost(String size) {
        return switch (size) {
            case "4" -> 0.30;
            case "8" -> 0.60;
            case "12" -> 0.90;
            default -> 0.60;
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\" ").append(breadType).append(" ").append(getName()).append(" - ");
        sb.append(String.format("$%.2f", getPrice())).append("\n");
        for (ToppingCount tc : toppings) {
            sb.append("  - ").append(tc.topping.getName());
            if (tc.extraCount > 0) sb.append(" (extra x").append(tc.extraCount).append(")");
            sb.append("\n");
        }
        sb.append("  Toasted: ").append(toasted ? "Yes" : "No");
        return sb.toString();
    }

    /**
     * Helper class to pair a topping with its extra count.
     */

    public static class ToppingCount {
        public final Topping topping;
        public final int extraCount;
        public ToppingCount(Topping topping, int extraCount) {
            this.topping = topping;
            this.extraCount = extraCount;
        }
    }
}
