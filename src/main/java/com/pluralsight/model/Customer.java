package com.pluralsight.model;

/**
 * Customer entity with name and phone.
 */

public class Customer {
    private final String name;
    private final String phone;

    public Customer(String name, String phone) {
        this.name = (name == null || name.isBlank()) ? "Guest" : name;
        this.phone = (phone == null) ? "" : phone;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        if (phone.isBlank()) return name;
        return name + " (" + phone + ")";
    }
}
