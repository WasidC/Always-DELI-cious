package com.pluralsight.util;

import com.pluralsight.model.Discount;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads available discounts from data/discounts.csv.
 */

public class DiscountLoader {

    public static List<Discount> loadDiscounts(String filePath) {
        List<Discount> discounts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String code = parts[0].trim();
                double amount = Double.parseDouble(parts[1].trim());
                String type = parts[2].trim();
                String description = parts[3].trim();

                discounts.add(new Discount(code, amount, type, description));
            }
        }
        catch (Exception e) {
            System.out.println("Error loading discounts: " + e.getMessage());
        }

        return discounts;
    }
}
