package ru.hedw1q.DiplomaGroupingExtended.Service;

import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.DetailGroup;

import java.util.Arrays;
import java.util.LinkedHashMap;
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

    public LinkedHashMap<Integer, LinkedList<DetailGroup>> schedule(LinkedList<Detail> detailList) throws Exception {

        double k1, k2,avgAssemTime0 = 0;
        int maxCount = 9;
        int minCount = 3;
        int minDif = Integer.MAX_VALUE;
        int buf = 0,counter=0;

        LinkedHashMap<Integer, LinkedList<DetailGroup>> groupMap = new LinkedHashMap<>();
        LinkedHashMap<Integer, LinkedList<DetailGroup>> tempMap = new LinkedHashMap<>();

        for (int i = minCount; i <= maxCount / 2; i++) {
            for (int j = minCount; j <= maxCount / 2; j++) {

                LinkedList<Detail> firstGroup = new LinkedList<>(detailList.subList(0, i));
                LinkedList<Detail> secondGroup = new LinkedList<>(detailList.subList(i, firstGroup.size() + j));

                DetailGroup firstSlipGroupA = new DetailGroup(1);
                DetailGroup secondSlipGroupA = new DetailGroup(2);
                DetailGroup thirdSlipGroupA = new DetailGroup(3);
                DetailGroup firstSlipGroupB = new DetailGroup(1);
                DetailGroup secondSlipGroupB = new DetailGroup(2);
                DetailGroup thirdSlipGroupB = new DetailGroup(3);

                for (Detail d : firstGroup) {
                    if (d.getProduct_id() == 1) firstSlipGroupA.addDetail(d);
                    else if (d.getProduct_id() == 2) secondSlipGroupA.addDetail(d);
                    else if (d.getProduct_id() == 3) thirdSlipGroupA.addDetail(d);
                    else throw new RuntimeException("Превышено количество доступных стапелей");
                }
                for (Detail d : secondGroup) {
                    if (d.getProduct_id() == 1) firstSlipGroupB.addDetail(d);
                    else if (d.getProduct_id() == 2) secondSlipGroupB.addDetail(d);
                    else if (d.getProduct_id() == 3) thirdSlipGroupB.addDetail(d);
                    else throw new RuntimeException("Превышено количество доступных стапелей");
                }
                firstSlipGroupA.computeTimes();
                secondSlipGroupA.computeTimes();
                thirdSlipGroupA.computeTimes();

                firstSlipGroupB.computeTimes();
                secondSlipGroupB.computeTimes();
                thirdSlipGroupB.computeTimes();

                int groupProcTimeA = firstSlipGroupA.getProcTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime();
                int groupProcTimeB = firstSlipGroupB.getProcTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime();
                double avgAssemTimeA = (double) (firstSlipGroupA.getAssemTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime()) / 3;
                double avgAssemTimeB = (double) (firstSlipGroupB.getAssemTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime()) / 3;

                k1 = avgAssemTimeA / (firstSlipGroupA.getProcTime() + secondSlipGroupA.getProcTime() + thirdSlipGroupA.getProcTime());
                k2 = avgAssemTimeB / (firstSlipGroupB.getProcTime() + secondSlipGroupB.getProcTime() + thirdSlipGroupB.getProcTime());

                if (getMin(1, k1 * k2) <= k1) {

                    if (Math.abs(groupProcTimeB - avgAssemTimeA) < minDif) {
                        tempMap.clear();
                        minDif = (int) Math.abs(+groupProcTimeB - avgAssemTimeA);
                        LinkedList<DetailGroup> list1 = new LinkedList<>(Arrays.asList(firstSlipGroupA, secondSlipGroupA, thirdSlipGroupA));
                        LinkedList<DetailGroup> list2 = new LinkedList<>(Arrays.asList(firstSlipGroupB, secondSlipGroupB, thirdSlipGroupB));
                        buf = i + j;
                        tempMap.put(1, list1);
                        tempMap.put(2, list2);
                        avgAssemTime0 = avgAssemTimeB;
                    }
                }
            }
        }

        for (int i = 0; i < buf; i++) {
            detailList.removeFirst();
        }


        groupMap.put(++counter, tempMap.get(1));
        groupMap.put(++counter, tempMap.get(2));

        tempMap.clear();


        while (detailList.size() > 0) {
            minDif = Integer.MAX_VALUE;
            if (detailList.size() < 2 * minCount) {

                DetailGroup firstSlipGroup = new DetailGroup(1);
                DetailGroup secondSlipGroup = new DetailGroup(2);
                DetailGroup thirdSlipGroup = new DetailGroup(3);

                for (Detail d : detailList) {
                    if (d.getProduct_id() == 1) firstSlipGroup.addDetail(d);
                    else if (d.getProduct_id() == 2) secondSlipGroup.addDetail(d);
                    else if (d.getProduct_id() == 3) thirdSlipGroup.addDetail(d);
                    else throw new RuntimeException("Превышено количество доступных стапелей");
                    firstSlipGroup.computeTimes();
                    secondSlipGroup.computeTimes();
                    thirdSlipGroup.computeTimes();
                }
                LinkedList<DetailGroup> groupList = new LinkedList<>(Arrays.asList(firstSlipGroup, secondSlipGroup, thirdSlipGroup));

                groupMap.put(++counter, groupList);
                return groupMap;
            }

            for (int i = minCount; i <= maxCount; i++) {
                for (int j = minCount; j <= maxCount && (i + j) <= detailList.size(); j++) {
                    LinkedList<Detail> firstGroup = new LinkedList<>(detailList.subList(0, i));
                    LinkedList<Detail> secondGroup = new LinkedList<>(detailList.subList(i, firstGroup.size() + j));
                    DetailGroup firstSlipGroupA = new DetailGroup(1);
                    DetailGroup secondSlipGroupA = new DetailGroup(2);
                    DetailGroup thirdSlipGroupA = new DetailGroup(3);
                    DetailGroup firstSlipGroupB = new DetailGroup(1);
                    DetailGroup secondSlipGroupB = new DetailGroup(2);
                    DetailGroup thirdSlipGroupB = new DetailGroup(3);

                    for (Detail d : firstGroup) {
                        if (d.getProduct_id() == 1) firstSlipGroupA.addDetail(d);
                        else if (d.getProduct_id() == 2) secondSlipGroupA.addDetail(d);
                        else if (d.getProduct_id() == 3) thirdSlipGroupA.addDetail(d);
                        else throw new RuntimeException("Превышено количество доступных стапелей");
                    }
                    for (Detail d : secondGroup) {
                        if (d.getProduct_id() == 1) firstSlipGroupB.addDetail(d);
                        else if (d.getProduct_id() == 2) secondSlipGroupB.addDetail(d);
                        else if (d.getProduct_id() == 3) thirdSlipGroupB.addDetail(d);
                        else throw new RuntimeException("Превышено количество доступных стапелей");
                    }
                    firstSlipGroupA.computeTimes();
                    secondSlipGroupA.computeTimes();
                    thirdSlipGroupA.computeTimes();

                    firstSlipGroupB.computeTimes();
                    secondSlipGroupB.computeTimes();
                    thirdSlipGroupB.computeTimes();

                    int groupProcTimeA = firstSlipGroupA.getProcTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime();
                    int groupProcTimeB = firstSlipGroupB.getProcTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime();
                    double avgAssemTimeA = (double) (firstSlipGroupA.getAssemTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime()) / 3;
                    double avgAssemTimeB = (double) (firstSlipGroupB.getAssemTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime()) / 3;

                    k1 = avgAssemTimeA / (firstSlipGroupA.getProcTime() + secondSlipGroupA.getProcTime() + thirdSlipGroupA.getProcTime());
                    k2 = avgAssemTimeB / (firstSlipGroupB.getProcTime() + secondSlipGroupB.getProcTime() + thirdSlipGroupB.getProcTime());

                    if (getMin(1, k1 * k2) <= k1) {

                        if (Math.abs(groupProcTimeB - avgAssemTimeA + groupProcTimeA - avgAssemTime0) < minDif) {
                            tempMap.clear();
                            minDif = (int) Math.abs(+groupProcTimeB - avgAssemTimeA);
                            LinkedList<DetailGroup> list1 = new LinkedList<>(Arrays.asList(firstSlipGroupA, secondSlipGroupA, thirdSlipGroupA));
                            LinkedList<DetailGroup> list2 = new LinkedList<>(Arrays.asList(firstSlipGroupB, secondSlipGroupB, thirdSlipGroupB));
                            buf = i + j;
                            tempMap.put(1, list1);
                            tempMap.put(2, list2);
                            avgAssemTime0 = avgAssemTimeB;
                        }
                    }
                }
            }


            for (int i = 0; i < buf; i++) {
                detailList.removeFirst();
            }
            groupMap.put(++counter, tempMap.get(1));
            groupMap.put(++counter, tempMap.get(2));

            tempMap.clear();
        }
        System.out.println(groupMap);
        return groupMap;
    }

    private static double getMin(double n1, double n2) {
        if (n1 > n2) return n2;
        return n1;
    }


}
