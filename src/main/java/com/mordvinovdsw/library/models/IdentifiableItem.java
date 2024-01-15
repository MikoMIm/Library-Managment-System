package com.mordvinovdsw.library.models;

public class IdentifiableItem {
    private int id;
    private String name;
    private int quantity; // This could be the quantity or any other integer attribute.

    // Overloaded constructor for two parameters
    public IdentifiableItem(int id, String name) {
        this.id = id;
        this.name = name;
        this.quantity = 0; // You can default the quantity to 0 or any appropriate value.
    }

    // Existing constructor for three parameters
    public IdentifiableItem(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

