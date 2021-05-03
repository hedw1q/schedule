package ru.hedw1q.DiplomaGroupingExtended.Service;

/**
 * @author hedw1q
 */

import javafx.scene.control.Tab;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.DetailGroup;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

import static ru.hedw1q.DiplomaGroupingExtended.Entity.DetailGroup.getGenProcTime;

public class GanttChart extends JFrame {

    public GanttChart(String title) {
        super(title);
    }

    public Tab getGanttChartTab(IntervalCategoryDataset categoryDataset) {
        JFreeChart chart = ChartFactory.createGanttChart(
                "Диаграмма Гантта", // Chart title
                "", // X-Axis Label
                "", // Y-Axis Label
                categoryDataset);


        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRenderer(new MyRenderer());

        return new Tab("Диаграмма Гантта",
                new ChartViewer(chart));
    }

    public static IntervalCategoryDataset getCategoryDataset(Map<Integer, LinkedList<DetailGroup>> groupMap, LinkedList<Detail> detailList) {

        TaskSeries series1 = new TaskSeries("План");

        Task procTask = new Task("Механообработка",
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(0)).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(20 * 3600)).atZone(ZoneId.systemDefault()).toInstant())
        );
        Task assem1Task = new Task("Сборка: стапель 1",
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(0)).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(20 * 3600)).atZone(ZoneId.systemDefault()).toInstant())
        );
        Task assem2Task = new Task("Сборка: стапель 2",
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(0)).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(20 * 3600)).atZone(ZoneId.systemDefault()).toInstant())
        );
        Task assem3Task = new Task("Сборка: стапель 3",
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(0)).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(20 * 3600)).atZone(ZoneId.systemDefault()).toInstant())
        );
//Это начало всех каркасов
        int procBuf = 0;
        int assemBuf1, assemBuf2, assemBuf3;
        assemBuf1 = assemBuf2 = assemBuf3 = getGenProcTime(groupMap.get(1));

        detailList.clear();

        for (int i = 1; i <= groupMap.keySet().size(); i++) {
            if (assemBuf1 < procBuf) assemBuf1 = procBuf;
            if (assemBuf2 < procBuf) assemBuf2 = procBuf;
            if (assemBuf3 < procBuf) assemBuf3 = procBuf;

            // Это вообще для таблицы, в конце можно убрать

            LinkedList<Detail> subList1 = new LinkedList<>(groupMap.get(i).get(0).getDetails());
            subList1.addAll(groupMap.get(i).get(1).getDetails());
            subList1.addAll(groupMap.get(i).get(2).getDetails());

            for (Detail detail: subList1) {
                detail.setProcStart(Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * procBuf)).atZone(ZoneId.systemDefault()).toInstant()));
                detail.setProcEnd(Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (procBuf + getGenProcTime(groupMap.get(i))))).atZone(ZoneId.systemDefault()).toInstant()));
            }


            procTask.addSubtask(new Task("Механообработка",
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * procBuf)).atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (procBuf + getGenProcTime(groupMap.get(i))))).atZone(ZoneId.systemDefault()).toInstant())
            ));

            assem1Task.addSubtask(new Task("Сборка: стапель 1",
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * assemBuf1)).atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (assemBuf1 + groupMap.get(i).get(0).getAssemTime()))).atZone(ZoneId.systemDefault()).toInstant())
            ));
            assem2Task.addSubtask(new Task("Сборка: стапель 2",
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * assemBuf2)).atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (assemBuf2 + groupMap.get(i).get(1).getAssemTime()))).atZone(ZoneId.systemDefault()).toInstant())
            ));
            assem3Task.addSubtask(new Task("Сборка: стапель 3",
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * assemBuf3)).atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (assemBuf3 + groupMap.get(i).get(2).getAssemTime()))).atZone(ZoneId.systemDefault()).toInstant())
            ));


            procBuf += getGenProcTime(groupMap.get(i));
            assemBuf1 += groupMap.get(i).get(0).getAssemTime();
            assemBuf2 += groupMap.get(i).get(1).getAssemTime();
            assemBuf3 += groupMap.get(i).get(2).getAssemTime();
            
            detailList.addAll(subList1);

        }

        series1.add(procTask);
        series1.add(assem1Task);
        series1.add(assem2Task);
        series1.add(assem3Task);
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series1);

        return dataset;
    }

    private static class MyRenderer extends GanttRenderer {

        private final LinkedList<Color> clut = new LinkedList<>();

        public MyRenderer() {
            setShadowVisible(false);
        }

        public Paint getItemPaint(int row, int col) {
            if (clut.isEmpty()) initClut();
            ListIterator<Color> it = clut.listIterator();
            Color color = it.next();
            clut.remove(color);
            clut.addLast(color);
            return color;
        }

        //TODO:Если поварьировать количество цветов для конкретной задачи, то можно сделать нужные цвета
//TODO:Зависит от количества групп, надо для последней задачи шаманить
        private void initClut() {
            clut.addAll(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.DARK_GRAY));
        }
    }
}
