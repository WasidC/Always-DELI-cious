package com.pluralsight.io;

/**
 * Interface marking objects that can produce printable receipts.
 */

public interface IPrintable {
    /**
     * Produce a printable string representing the receipt/details.
     * @return printable string
     */

    String printReceipt();
}
