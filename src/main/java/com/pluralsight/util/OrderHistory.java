package com.pluralsight.util;

import com.pluralsight.model.Order;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

/**
 * Simple order history manager: writes a readable history.txt and a CSV summary orders.csv.
 */

public class OrderHistory {

    private final Path historyFile;
    private final Path csvFile;

    public OrderHistory(Path dir) throws Exception {
        if (!Files.exists(dir)) Files.createDirectories(dir);
        this.historyFile = dir.resolve("history.txt");
        this.csvFile = dir.resolve("orders.csv");
        if (!Files.exists(csvFile)) {
            Files.writeString(csvFile, "timestamp,total,items,customer\n", StandardCharsets.UTF_8);
        }
    }

    /**
     * Append an order summary to history files.
     */

    public void record(Order order) {
        try {
            // Append human readable
            try (BufferedWriter w = Files.newBufferedWriter(historyFile, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
                w.write(order.printReceipt());
                w.write("\n---\n");
            }
            // Append CSV line
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String ts = order.getCreated().format(f);
            String items = order.getItems().size() + "";
            double total = order.calculateTotal();
            String customer = order.getCustomer() == null ? "" : order.getCustomer().getName();
            String csvLine = String.format("\"%s\",%.2f,%s,\"%s\"\n", ts, total, items, customer);
            Files.writeString(csvFile, csvLine, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("[OrderHistory] Failed to record order: " + e.getMessage());
        }
    }
}
