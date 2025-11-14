package com.pluralsight.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu holds signature sandwiches and optionally other menu items.
 */

public class Menu {
    private final List<SignatureSandwich> signatures = new ArrayList<>();

    public void addSignature(SignatureSandwich s) {
        signatures.add(s);
    }

    public List<SignatureSandwich> getSignatures() {
        return List.copyOf(signatures);
    }

    public void clear() {
        signatures.clear();
    }
}
