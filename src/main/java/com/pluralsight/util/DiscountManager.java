package com.pluralsight.util;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * DiscountManager loads discount codes and applies discounts.
 * discounts.csv format:
 * CODE,TYPE,VALUE,DESCRIPTION
 * TYPE: PERCENT or FIXED
 * VALUE: for PERCENT use 10 for 10%, for FIXED use 2.5 etc.
 * Example:
 * SPRING10,PERCENT,10,10% off everything
 * FIVEOFF,FIXED,5.0,$5 off orders over $20
 */

public class DiscountManager {
    private final Map<String, Discount> discounts = new HashMap<>();

    public static class Discount {
        public final String code;
        public final String type; // PERCENT or FIXED
        public final double value;
        public final String description;

        public Discount(String code, String type, double value, String description) {
            this.code = code;
            this.type = type;
            this.value = value;
            this.description = description;
        }
    }

    public void loadFromCsv(Path csvPath) {
        try {
            if (!Files.exists(csvPath)) {
                System.out.println("[DiscountManager] File not found: " + csvPath + " (skipping)");
                return;
            }
            try (BufferedReader r = Files.newBufferedReader(csvPath)) {
                String line;
                while ((line = r.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] p = line.split(",", 4);
                    if (p.length < 3) continue;
                    String code = p[0].trim().toUpperCase();
                    String type = p[1].trim().toUpperCase();
                    double val = 0;
                    try { val = Double.parseDouble(p[2].trim()); } catch (Exception ignored) {}
                    String desc = p.length >= 4 ? p[3].trim() : "";
                    discounts.put(code, new Discount(code, type, val, desc));
                }
            }
        } catch (Exception e) {
            System.out.println("[DiscountManager] Error loading discounts: " + e.getMessage());
        }
    }

    public Discount getDiscount(String code) {
        if (code == null) return null;
        return discounts.get(code.trim().toUpperCase());
    }

    /**
     * Apply discount to an order total and return new total.
     * Note: does NOT persist application to order object; caller should handle.
     */

    public double applyDiscount(double total, Discount d) {
        if (d == null) return total;
        return switch (d.type) {
            case "PERCENT" -> Math.max(0.0, total - (total * (d.value / 100.0)));
            case "FIXED" -> Math.max(0.0, total - d.value);
            default -> total;
        };
    }
}
