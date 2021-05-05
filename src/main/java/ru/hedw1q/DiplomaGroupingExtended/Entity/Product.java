package ru.hedw1q.DiplomaGroupingExtended.Entity;

import java.util.List;

public class Product {
    private int id;
    private String name;
    private List<Detail> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }
}
