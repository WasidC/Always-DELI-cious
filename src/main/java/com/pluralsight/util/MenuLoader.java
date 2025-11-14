package com.pluralsight.util;

import com.pluralsight.model.Menu;
import com.pluralsight.model.SignatureSandwich;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple CSV menu loader.
 * CSV format (signature sandwiches):
 * name,size,breaddType,topping1|type|extra; topping2|type|extra;...
 * Example:
 * BLT,8,WHITE,Bacon|Meat|0;Cheddar|Cheese|0;Lettuce|Vegetable|0;Tomato|Vegetable|0;Ranch|Sauce|0
 */

public class MenuLoader {

    /**
     * Load signature sandwiches into the given Menu from a CSV file.
     * If file is missing or malformed, method prints errors but continues.
     * @param menu menu instance to populate
     * @param csvPath path to CSV file
     */
    public static void loadSignaturesFromCsv(Menu menu, Path csvPath) {
        try {
            if (!Files.exists(csvPath)) {
                System.out.println("[MenuLoader] File not found: " + csvPath + " (skipping)");
                return;
            }
            try (BufferedReader r = Files.newBufferedReader(csvPath)) {
                String line;
                while ((line = r.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] parts = line.split(",", 4);
                    if (parts.length < 4) {
                        System.out.println("[MenuLoader] Malformed line (skip): " + line);
                        continue;
                    }
                    String name = parts[0].trim();
                    String size = parts[1].trim();
                    String bread = parts[2].trim();
                    String toppingsStr = parts[3].trim();
                    SignatureSandwich s = new SignatureSandwich(name, size, bread);
                    if (!toppingsStr.isEmpty()) {
                        String[] tparts = toppingsStr.split(";");
                        for (String tp : tparts) {
                            String[] tfields = tp.trim().split("\\|");
                            if (tfields.length < 2) continue;
                            String tname = tfields[0].trim();
                            String ttype = tfields[1].trim().toLowerCase();
                            int extra = 0;
                            if (tfields.length >= 3) {
                                try { extra = Integer.parseInt(tfields[2].trim()); } catch (Exception ignored) {}
                            }
                            switch (ttype) {
                                case "meat" -> s.addTopping(new com.pluralsight.model.MeatTopping(tname), extra);
                                case "cheese" -> s.addTopping(new com.pluralsight.model.CheeseTopping(tname), extra);
                                case "vegetable" -> s.addTopping(new com.pluralsight.model.VegetableTopping(tname), extra);
                                case "sauce" -> s.addTopping(new com.pluralsight.model.SauceTopping(tname), extra);
                                case "side" -> s.addTopping(new com.pluralsight.model.SideTopping(tname), extra);
                                default -> s.addTopping(new com.pluralsight.model.VegetableTopping(tname), extra);
                            }
                        }
                    }
                    menu.addSignature(s);
                }
            }
        } catch (Exception e) {
            System.out.println("[MenuLoader] Error loading menu: " + e.getMessage());
        }
    }
}
