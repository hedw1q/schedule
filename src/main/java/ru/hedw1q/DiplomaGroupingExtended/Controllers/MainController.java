package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;
import ru.hedw1q.DiplomaGroupingExtended.Service.ScheduleHandler;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;
import ru.hedw1q.DiplomaGroupingExtended.Service.GanttChart;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static ru.hedw1q.DiplomaGroupingExtended.Entity.DetailGroup.*;

public class MainController implements Initializable {


    @FXML
    ComboBox<String> productComboBox;

    @FXML
    public void redirectToProduct(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Product.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Создание нового изделия");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void redirectToDetail(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Detail.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Создание новой детали");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void redirectToAnalysis(ActionEvent event) throws Exception {
        DAO dao = DAO.getInstance();
        ScheduleHandler scheduleHandler = ScheduleHandler.getInstance();

        Product product = dao.getProductByName(productComboBox.getValue());
        LinkedList<Detail> detailList = new LinkedList<>(product.getDetails());
        LinkedList<LinkedList<Detail>> groupList = scheduleHandler.schedule(detailList);
        System.out.println(groupList);
        plotGanttChart(getCategoryDataset(groupList));

    }

    private IntervalCategoryDataset getCategoryDataset(LinkedList<LinkedList<Detail>> groupList) {

        TaskSeries series1 = new TaskSeries("Scheduled");

        Task procTask = new Task("Механообработка",
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(0)).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(70000)).atZone(ZoneId.systemDefault()).toInstant())
        );
        Task assemTask = new Task("Сборка",
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(0)).atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(70000)).atZone(ZoneId.systemDefault()).toInstant())
        );
        int procBuf = 0;
        int assemBuf = getSumProc(groupList.get(0));
        for (int i = 0; i < groupList.size(); i++) {

            LinkedList<Detail> linkedList = new LinkedList<>(groupList.get(i));
            if (assemBuf < (procBuf + getSumProc(linkedList))) {
                assemBuf = procBuf + getSumProc(linkedList);
            }
            procTask.addSubtask(new Task("Механообработка",
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * procBuf)).atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (procBuf + getSumProc(linkedList)))).atZone(ZoneId.systemDefault()).toInstant())
            ));
            assemTask.addSubtask(new Task("Сборка",
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * assemBuf)).atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(LocalDate.now().atTime(LocalTime.ofSecondOfDay(60 * (assemBuf + getSumAssem(linkedList)))).atZone(ZoneId.systemDefault()).toInstant())
            ));
            procBuf += getSumProc(linkedList);
            assemBuf += getSumAssem(linkedList);
        }

        series1.add(procTask);
        series1.add(assemTask);
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series1);

        return dataset;
    }

    public static void plotGanttChart(IntervalCategoryDataset categoryDataset){
        SwingUtilities.invokeLater(() -> {
            GanttChart example = new GanttChart("Диаграмма Гантта", categoryDataset);
            example.setSize(1600, 300);
            example.setLocationRelativeTo(null);
            example.setVisible(true);
        });
    }

    public void createRandomDetails(ActionEvent event) {
        Random random = new Random();
        DAO dao = DAO.getInstance();
        dao.deleteAllDetails();
        for (int i = 1; i < 61; i++) {
            Detail detail = new Detail();
            detail.setName("d" + i);
            detail.setAssemTime(random.nextInt(25) + 5);
            detail.setProcTime(random.nextInt(25) + 5);
            detail.setId(i);
            detail.setProduct_id(1);
            dao.createDetail(detail);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAO dao = DAO.getInstance();
        List<String> productNamesList = dao.getProductNamesList();
        productComboBox.getItems().addAll(productNamesList);
    }
}
