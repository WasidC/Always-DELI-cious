package com.pluralsight.io;

import com.pluralsight.model.Order;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

/**
 * Timestamped receipts.
 */

public class ReceiptGenerator {

    /**
     * Save an order receipt to a timestamped file in the given directory.
     * @param order order to save
     * @param directory directory path (will be created if missing)
     * @return path to created file
     * @throws Exception on IO errors
     */

    public static Path saveOrderReceipt(Order order, String directory) throws Exception {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String fname = order.getCreated().format(fmt) + ".txt";
        Path dir = Path.of(directory);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Path file = dir.resolve(fname);
        String content = order.printReceipt();
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }
}
