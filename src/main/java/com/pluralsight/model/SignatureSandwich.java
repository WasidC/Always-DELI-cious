package com.pluralsight.model;

/**
 * Signature sandwich extends Sandwich
 */

public class SignatureSandwich extends Sandwich {
    private final String signatureName;

    public SignatureSandwich(String signatureName, String size, String breadType) {
        super(size, breadType);
        this.signatureName = (signatureName == null || signatureName.isBlank()) ? "Signature" : signatureName;
        this.name = this.signatureName;
    }

    @Override
    public String toString() {
        return "Signature - " + signatureName + "\n" + super.toString();
    }
}
