package ru.hedw1q.DiplomaGroupingExtended.Service;

/**
 * @author hedw1q
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class GanttChart extends JFrame {
    public GanttChart(String title, IntervalCategoryDataset categoryDataset) {
        super(title);

        // Create chart
        JFreeChart chart = ChartFactory.createGanttChart(
                "Диаграмма Гантта", // Chart title
                "", // X-Axis Label
                "", // Y-Axis Label
                categoryDataset);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRenderer(new MyRenderer());

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }


    private static class MyRenderer extends GanttRenderer {

        private final LinkedList<Color> clut = new LinkedList<>();

        public MyRenderer() {
        }

        public Paint getItemPaint(int row,int col) {
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
            clut.addAll(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE,Color.YELLOW,Color.DARK_GRAY,Color.CYAN,Color.BLACK,Color.PINK,Color.BLUE,Color.white,Color.blue));
        }
    }
}
