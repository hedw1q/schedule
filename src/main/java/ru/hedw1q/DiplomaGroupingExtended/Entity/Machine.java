package ru.hedw1q.DiplomaGroupingExtended.Entity;

/**
 * @author hedw1q
 */
public class Machine {
    private int id;
    private String name;

    public Machine(int id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
