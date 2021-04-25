package ru.hedw1q.DiplomaGroupingExtended.Service;

import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;

import java.util.LinkedList;

/**
 * @author hedw1q
 */
public class ScheduleHandler {

    private static ScheduleHandler instance;

    private ScheduleHandler() {
    }

    public static ScheduleHandler getInstance() {
        if (instance == null) {
            instance = new ScheduleHandler();
        }
        return instance;
    }

    public LinkedList<LinkedList<Detail>> schedule(LinkedList<Detail> detailList) {
        int groupProcTime1 = 0, groupProcTime2 = 0;
        int groupAssemTime1 = 0, groupAssemTime2 = 0;
        int groupAssemTime0=0;
        double k1, k2;
        int maxCount = 7;
        int minCount = 3;
        //Список групп в порядке изготовления
        LinkedList<LinkedList<Detail>> groupList = new LinkedList<>();
        //Промежуточный лист
        LinkedList<LinkedList<Detail>> tempList=new LinkedList<>();
        //Разница между временем изготовления и сборки для 2 групп сразу
        int minDif = Integer.MAX_VALUE;

        for (int i = minCount; i <= maxCount; i++) {
            for (int j = minCount; j <= maxCount; j++) {

                LinkedList<Detail> firstGroup = new LinkedList<>(detailList.subList(0, i));
                LinkedList<Detail> secondGroup = new LinkedList<>(detailList.subList(i, firstGroup.size() + j));
                System.out.print(firstGroup.size() +" "+secondGroup.size());
                for (Detail d : firstGroup) {
                    groupProcTime1 += d.getProcTime();
                    groupAssemTime1 += d.getAssemTime();
                }
                for (Detail d : secondGroup) {
                    groupProcTime2 += d.getProcTime();
                    groupAssemTime2 += d.getAssemTime();
                }
                k1 = (double) groupAssemTime1 / groupProcTime1;
                k2 = (double) groupAssemTime2 / groupProcTime2;
                if (getMin(1, k1 * k2) <= k1) {

                    System.out.print(" Условие выполняется ");
                    if (Math.abs(+groupProcTime2 - groupAssemTime1) < minDif) {
                        tempList.clear();
                        minDif = Math.abs(+groupProcTime2 - groupAssemTime1);
                        tempList.add(firstGroup);
                        tempList.add(secondGroup);
                        groupAssemTime0=groupAssemTime2;
                        System.out.print(minDif);
                    }

                    System.out.println();
                } else System.out.println(" Условие не выполняется ");
            }
        }
        int buf = tempList.get(0).size() + tempList.get(1).size();
        for (int i = 0; i < buf; i++) {
            detailList.removeFirst();
        }
//        DetailGroup detailGroup1=new DetailGroup(tempList.getFirst());
//        DetailGroup detailGroup2=new DetailGroup(tempList.get(1));
//
//        detailGroup1.setProcStartsOn(0);
//        detailGroup1.setProcEndsOn(getSumProc(tempList.get(0)));
//        detailGroup1.setAssemStartsOn(detailGroup1.getProcEndsOn());
//        detailGroup1.setAssemEndsOn(detailGroup1.getAssemStartsOn()+getSumAssem(tempList.get(0)));
//
//        detailGroup2.setProcStartsOn(detailGroup1.getProcEndsOn());
//        detailGroup2.setProcEndsOn(detailGroup2.getProcStartsOn()+getSumProc(tempList.get(1)));


        groupList.addAll(tempList);
        tempList.clear();
        minDif=1000000;
        System.out.println(detailList);
        //На выходе: detailList минус первые 2 группы деталей,
        // groupList имеет первые 2 группы деталей
        System.out.println("==========================================================================");

while(detailList.size()>0) {
minDif=Integer.MAX_VALUE;
    if(detailList.size()<2*minCount){
        groupList.add(detailList);
        System.out.println("Детали закончились");
        return groupList;
    }

    for (int i = minCount; i <= maxCount; i++) {
        for (int j = minCount; j <= maxCount && (i + j) <= detailList.size(); j++) {

            LinkedList<Detail> firstGroup = new LinkedList<>(detailList.subList(0, i));
            LinkedList<Detail> secondGroup = new LinkedList<>(detailList.subList(i, firstGroup.size() + j));
            System.out.print(firstGroup.size() + " " + secondGroup.size());
            for (Detail d : firstGroup) {
                groupProcTime1 += d.getProcTime();
                groupAssemTime1 += d.getAssemTime();
            }
            for (Detail d : secondGroup) {
                groupProcTime2 += d.getProcTime();
                groupAssemTime2 += d.getAssemTime();
            }
            k1 = (double) groupAssemTime1 / groupProcTime1;
            k2 = (double) groupAssemTime2 / groupProcTime2;
            if (getMin(1, k1 * k2) <= k1) {

                System.out.print(" Условие выполняется ");
                if (Math.abs(groupProcTime1 + groupProcTime2 - groupAssemTime1 - groupAssemTime0) < minDif) {
                    tempList.clear();
                    minDif = Math.abs(groupProcTime1 + groupProcTime2 - groupAssemTime1 - groupAssemTime0);
                    tempList.add(firstGroup);
                    tempList.add(secondGroup);
                    groupAssemTime0 = groupAssemTime2;
                    System.out.print(minDif);
                }

                System.out.println();
            } else System.out.println(" Условие не выполняется ");
        }
    }
    int buf1=tempList.get(0).size();
    int buf2=tempList.get(1).size();
    for (int i = 0; i <buf1 +buf2; i++) {
        detailList.removeFirst();
    }
    System.out.println(tempList);
    groupList.addAll(tempList);
    tempList.clear();
    System.out.println(detailList);
    System.out.println("==========================================================================");

}
        return groupList;
    }

    private static double getMin(double n1, double n2) {
        if (n1 > n2) return n2;
        return n1;
    }


}
