package ru.hedw1q.DiplomaGroupingExtended.Entity;

public class Detail {
    /**
     * All times in minutes
     */
    private int id;
    private String name;
   private int procTime;
   private int assemTime;
private int product_id;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public Detail() {
    }

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

    public int getProcTime() {
        return procTime;
    }

    public void setProcTime(int procTime) {
        this.procTime = procTime;
    }

    public int getAssemTime() {
        return assemTime;
    }

    public void setAssemTime(int assemTime) {
        this.assemTime = assemTime;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
