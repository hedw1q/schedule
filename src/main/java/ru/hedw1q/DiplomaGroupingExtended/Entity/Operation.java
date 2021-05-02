package ru.hedw1q.DiplomaGroupingExtended.Entity;

/**
 * @author hedw1q
 */
public class Operation {
    private Machine machine;
    private int time;

    public Operation(Machine machine, int time) {
        this.machine = machine;
        this.time = time;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "machine=" + machine +
                ", time=" + time;
    }
}
