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

    /*Мысли:
    1. У каждой детали есть свой slipNumber=product_id;
    2. Сделать список деталей в таком порядке: 1.1,2.1,3.1,1.2,2.2,2.3...
    3. Так же брать первые 2 группы от 9 деталей до 21
    4. Создать 3 объекта DetailGroup,у которого есть параметры номер стапеля, время сборки и время изготовления и ki - 2 раза
    5. Вычислить средний коэффициент k' - 2 раза
    6. Проверка условия Джонсона
    7. Вычисление отклонения
    8. На выходе LinkedHashMap<int(номер группы), LinkedList<DetailGroup>(список DetailGroup для каждого стапеля)>
     */
    public LinkedHashMap<Integer, LinkedList<DetailGroup>> schedule(LinkedList<Detail> detailList) throws Exception {
//        int groupProcTime1 = 0, groupProcTime2 = 0;
//        int groupAssemTime1 = 0, groupAssemTime2 = 0;
        //  int groupAssemTime0=0;
        double k1, k2;
        int maxCount = 9;
        int minCount = 3;
        //Список групп в порядке изготовления
        LinkedHashMap<Integer, LinkedList<DetailGroup>> groupMap = new LinkedHashMap<>();        //Промежуточный лист
        LinkedHashMap<Integer, LinkedList<DetailGroup>> tempMap = new LinkedHashMap<>();
        //  LinkedList<LinkedList<Detail>> tempList = new LinkedList<>();
        //Разница между временем изготовления и сборки для 2 групп сразу
        int minDif = Integer.MAX_VALUE;
        int buf = 0;
        double avgAssemTime0 = 0;
        int counter = 0;

        for (int i = minCount; i <= maxCount / 2; i++) {
            for (int j = minCount; j <= maxCount / 2; j++) {

                LinkedList<Detail> firstGroup = new LinkedList<>(detailList.subList(0, i));
                LinkedList<Detail> secondGroup = new LinkedList<>(detailList.subList(i, firstGroup.size() + j));
                //Детали для группы 1 в зависимости от стапеля
                DetailGroup firstSlipGroupA = new DetailGroup(1);
                DetailGroup secondSlipGroupA = new DetailGroup(2);
                DetailGroup thirdSlipGroupA = new DetailGroup(3);
                //Детали для группы 2 в зависимости от стапеля
                DetailGroup firstSlipGroupB = new DetailGroup(1);
                DetailGroup secondSlipGroupB = new DetailGroup(2);
                DetailGroup thirdSlipGroupB = new DetailGroup(3);

                System.out.print(firstGroup.size() + " " + secondGroup.size());

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
                //    System.out.print(" I:"+firstSlipGroupA+" "+secondSlipGroupA+" "+thirdSlipGroupA);
                //   System.out.println(" II:"+firstSlipGroupB+" "+secondSlipGroupB+" "+thirdSlipGroupB);

//                for (Detail d : firstGroup) {
//                    groupProcTime1 += d.getProcTime();
//                    groupAssemTime1 += d.getAssemTime();
//                }
//                for (Detail d : secondGroup) {
//                    groupProcTime2 += d.getProcTime();
//                    groupAssemTime2 += d.getAssemTime();
//                }
                int groupProcTimeA = firstSlipGroupA.getProcTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime();
                int groupProcTimeB = firstSlipGroupB.getProcTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime();
                double avgAssemTimeA = (double) (firstSlipGroupA.getAssemTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime()) / 3;
                double avgAssemTimeB = (double) (firstSlipGroupB.getAssemTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime()) / 3;

                k1 = avgAssemTimeA / (firstSlipGroupA.getProcTime() + secondSlipGroupA.getProcTime() + thirdSlipGroupA.getProcTime());
                k2 = avgAssemTimeB / (firstSlipGroupB.getProcTime() + secondSlipGroupB.getProcTime() + thirdSlipGroupB.getProcTime());

                if (getMin(1, k1 * k2) <= k1) {

                    // System.out.print(" Условие выполняется ");
                    if (Math.abs(groupProcTimeB - avgAssemTimeA) < minDif) {
                        tempMap.clear();
                        minDif = (int) Math.abs(+groupProcTimeB - avgAssemTimeA);
                        //потом убрать листы для краосты
                        LinkedList<DetailGroup> list1 = new LinkedList<>(Arrays.asList(firstSlipGroupA, secondSlipGroupA, thirdSlipGroupA));
                        LinkedList<DetailGroup> list2 = new LinkedList<>(Arrays.asList(firstSlipGroupB, secondSlipGroupB, thirdSlipGroupB));
                        buf = i + j;
                        tempMap.put(1, list1);
                        tempMap.put(2, list2);
                        avgAssemTime0 = avgAssemTimeB;
                        System.out.print(" " + minDif);
                        //  System.out.println(tempMap);
                    }
                    System.out.println();
                } //else System.out.println(" Условие не выполняется ");
            }
        }
        System.out.println(tempMap);

        for (int i = 0; i < buf; i++) {
            detailList.removeFirst();
        }


        groupMap.put(++counter, tempMap.get(1));
        groupMap.put(++counter, tempMap.get(2));

        tempMap.clear();
        System.out.println(detailList);

        System.out.println("======================================================================================================");

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
                }
                LinkedList<DetailGroup> groupList = new LinkedList<>(Arrays.asList(firstSlipGroup, secondSlipGroup, thirdSlipGroup));

                groupMap.put(++counter, groupList);
                System.out.println("Детали закончились");
                return groupMap;
            }

            for (int i = minCount; i <= maxCount; i++) {
                for (int j = minCount; j <= maxCount && (i + j) <= detailList.size(); j++) {
                    LinkedList<Detail> firstGroup = new LinkedList<>(detailList.subList(0, i));
                    LinkedList<Detail> secondGroup = new LinkedList<>(detailList.subList(i, firstGroup.size() + j));
                    //Детали для группы 1 в зависимости от стапеля
                    DetailGroup firstSlipGroupA = new DetailGroup(1);
                    DetailGroup secondSlipGroupA = new DetailGroup(2);
                    DetailGroup thirdSlipGroupA = new DetailGroup(3);
                    //Детали для группы 2 в зависимости от стапеля
                    DetailGroup firstSlipGroupB = new DetailGroup(1);
                    DetailGroup secondSlipGroupB = new DetailGroup(2);
                    DetailGroup thirdSlipGroupB = new DetailGroup(3);

                    System.out.print(firstGroup.size() + " " + secondGroup.size());

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
                    //    System.out.print(" I:"+firstSlipGroupA+" "+secondSlipGroupA+" "+thirdSlipGroupA);
                    //   System.out.println(" II:"+firstSlipGroupB+" "+secondSlipGroupB+" "+thirdSlipGroupB);

//                for (Detail d : firstGroup) {
//                    groupProcTime1 += d.getProcTime();
//                    groupAssemTime1 += d.getAssemTime();
//                }
//                for (Detail d : secondGroup) {
//                    groupProcTime2 += d.getProcTime();
//                    groupAssemTime2 += d.getAssemTime();
//                }
                    int groupProcTimeA = firstSlipGroupA.getProcTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime();
                    int groupProcTimeB = firstSlipGroupB.getProcTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime();
                    double avgAssemTimeA = (double) (firstSlipGroupA.getAssemTime() + secondSlipGroupA.getAssemTime() + thirdSlipGroupA.getAssemTime()) / 3;
                    double avgAssemTimeB = (double) (firstSlipGroupB.getAssemTime() + secondSlipGroupB.getAssemTime() + thirdSlipGroupB.getAssemTime()) / 3;

                    k1 = avgAssemTimeA / (firstSlipGroupA.getProcTime() + secondSlipGroupA.getProcTime() + thirdSlipGroupA.getProcTime());
                    k2 = avgAssemTimeB / (firstSlipGroupB.getProcTime() + secondSlipGroupB.getProcTime() + thirdSlipGroupB.getProcTime());

                    if (getMin(1, k1 * k2) <= k1) {

                        // System.out.print(" Условие выполняется ");
                        if (Math.abs(groupProcTimeB - avgAssemTimeA + groupProcTimeA - avgAssemTime0) < minDif) {
                            tempMap.clear();
                            minDif = (int) Math.abs(+groupProcTimeB - avgAssemTimeA);
                            //потом убрать листы для краосты
                            LinkedList<DetailGroup> list1 = new LinkedList<>(Arrays.asList(firstSlipGroupA, secondSlipGroupA, thirdSlipGroupA));
                            LinkedList<DetailGroup> list2 = new LinkedList<>(Arrays.asList(firstSlipGroupB, secondSlipGroupB, thirdSlipGroupB));
                            buf = i + j;
                            tempMap.put(1, list1);
                            tempMap.put(2, list2);
                            avgAssemTime0 = avgAssemTimeB;
                            System.out.print(" " + minDif);
                            //  System.out.println(tempMap);
                        }
                        System.out.println();
                    } //else System.out.println(" Условие не выполняется ");
                }
            }


            for (int i = 0; i < buf; i++) {
                detailList.removeFirst();
            }
            groupMap.put(++counter, tempMap.get(1));
            groupMap.put(++counter, tempMap.get(2));

            tempMap.clear();

            System.out.println(detailList);
            System.out.println("==========================================================================");

        }
        return groupMap;
    }

    private static double getMin(double n1, double n2) {
        if (n1 > n2) return n2;
        return n1;
    }


}
