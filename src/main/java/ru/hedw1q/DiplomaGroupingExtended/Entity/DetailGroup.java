package ru.hedw1q.DiplomaGroupingExtended.Entity;

import java.util.LinkedList;

/**
 * @author hedw1q
 */
public class DetailGroup {
    private LinkedList<Detail> details;
    private int procStartsOn;
    private int procEndsOn;
    private int assemStartsOn;
    private int assemEndsOn;

    public static int getSumProc(LinkedList<Detail> list){
        int buf=0;
        for(Detail detail:list){
             buf+=detail.getProcTime();
        }
        return buf;
    }
    public static int getSumAssem(LinkedList<Detail> list){
        int buf=0;
        for(Detail detail:list){
            buf+=detail.getAssemTime();
        }
        return buf;
    }

    public DetailGroup() {
    }

    public DetailGroup(LinkedList<Detail> details) {
        this.details = details;
    }

    public LinkedList<Detail> getDetails() {
        return details;
    }

    public void setDetails(LinkedList<Detail> detailList) {
        this.details = detailList;
    }

    public int getProcStartsOn() {
        return procStartsOn;
    }

    public void setProcStartsOn(int procStartsOn) {
        this.procStartsOn = procStartsOn;
    }

    public int getProcEndsOn() {
        return procEndsOn;
    }

    public void setProcEndsOn(int procEndsOn) {
        this.procEndsOn = procEndsOn;
    }

    public int getAssemStartsOn() {
        return assemStartsOn;
    }

    public void setAssemStartsOn(int assemStartsOn) {
        this.assemStartsOn = assemStartsOn;
    }

    public int getAssemEndsOn() {
        return assemEndsOn;
    }

    public void setAssemEndsOn(int assemEndsOn) {
        this.assemEndsOn = assemEndsOn;
    }
}
