package ru.hedw1q.DiplomaGroupingExtended.Entity;

import java.util.LinkedList;

/**
 * @author hedw1q
 */
public class DetailGroup {

    private int procTime;
    private int assemTime;
    private int slipNumber;
    private LinkedList<Detail> details = new LinkedList<>();


    public DetailGroup(int slipNumber) {
        this.slipNumber = slipNumber;
    }

    public DetailGroup(LinkedList<Detail> details, int slipNumber) {
        this.details = details;
        computeTimes();
        this.slipNumber = slipNumber;
    }

    public void computeTimes() {
        this.procTime = getSumProc(details);
        this.assemTime = getSumAssem(details);
    }


    public void addDetail(Detail detail) {
        getDetails().add(detail);
    }

    public LinkedList<Detail> getDetails() {
        return details;
    }

    public void setDetails(LinkedList<Detail> details) {
        this.details = details;
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

    public int getSlipNumber() {
        return slipNumber;
    }

    public void setSlipNumber(int slipNumber) {
        this.slipNumber = slipNumber;
    }

    public static int getSumProc(LinkedList<Detail> list) {
        int buf = 0;
        for (Detail detail : list) {
            buf += detail.getProcTime();
        }
        return buf;
    }

    public static int getSumAssem(LinkedList<Detail> list) {
        int buf = 0;
        for (Detail detail : list) {
            buf += detail.getAssemTime();
        }
        return buf;
    }

    public static int getGenProcTime(LinkedList<DetailGroup> groupList) {
        int time = 0;
        for (DetailGroup detailGroup : groupList) {
            time += detailGroup.procTime;
        }
        return time;
    }

    @Override
    public String toString() {
        return details.toString();
    }
}
