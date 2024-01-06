package com.mordvinovdsw.library.models;

public class IdentifiableItem {
    private final int id;

    private final String name;

    public IdentifiableItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

