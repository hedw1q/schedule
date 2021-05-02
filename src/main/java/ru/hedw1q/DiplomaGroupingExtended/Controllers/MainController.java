package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.category.IntervalCategoryDataset;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.DetailGroup;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;
import ru.hedw1q.DiplomaGroupingExtended.Service.Planner;
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
import static ru.hedw1q.DiplomaGroupingExtended.Service.GanttChart.getCategoryDataset;

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


        LinkedList<Detail> detailList = new LinkedList<>();


        LinkedList<Detail> detA = new LinkedList<>(dao.getDetailsByProductId(1));
        LinkedList<Detail> detB = new LinkedList<>(dao.getDetailsByProductId(2));
        LinkedList<Detail> detC = new LinkedList<>(dao.getDetailsByProductId(3));

        for (int i = 0; i < detA.size(); i++) {
            detailList.add(detA.get(i));
            detailList.add(detB.get(i));
            detailList.add(detC.get(i));
        }
        LinkedList<Detail> copyDetailList = new LinkedList<>(detailList);
        System.out.println(detailList);


        Map<Integer, LinkedList<DetailGroup>> groupMap = scheduleHandler.schedule(detailList);
        System.out.println(groupMap);

        new Planner().planOrder(new GanttChart("Диаграмма Гантта").getGanttChartTab(getCategoryDataset(groupMap)),
                createInfoTable(copyDetailList));

    }

    public static Tab createInfoTable(List<Detail> detailList) {

        TableView<Detail> tableView = new TableView();

        TableColumn<Detail, Integer> idCol = new TableColumn<>("ID Детали");
        TableColumn<Detail, String> nameCol = new TableColumn<>("Наименование");
        TableColumn<Detail, Integer> product_idCol = new TableColumn<>("ID изделия");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_idCol.setCellValueFactory(new PropertyValueFactory<>("product_id"));

        tableView.getColumns().addAll(idCol, nameCol, product_idCol);


        tableView.setItems(FXCollections.observableList(detailList));

        Tab tab1 = new Tab("Информация о деталях");

        tab1.setContent(tableView);

        return tab1;
    }

    public static void plotGanttChart(IntervalCategoryDataset categoryDataset) {

//        SwingUtilities.invokeLater(() -> {
//            GanttChart example = new GanttChart("Диаграмма Гантта", categoryDataset);
//            example.setSize(1600, 300);
//            example.setLocationRelativeTo(null);
//            example.setVisible(true);

//        });
    }

    public void createRandomDetails(ActionEvent event) {
        Random random = new Random();
        DAO dao = DAO.getInstance();
        dao.deleteAllDetails();
        for (int i = 1; i < 21; i++) {
            Detail detail = new Detail();
            detail.setName("d" + i);
            detail.setAssemTime(random.nextInt(65) + 5);
            detail.setProcTime(random.nextInt(25) + 5);
            detail.setId(i);
            detail.setProduct_id(1);
            dao.createDetail(detail);
        }
        for (int i = 21; i < 41; i++) {
            Detail detail = new Detail();
            detail.setName("d" + i);
            detail.setAssemTime(random.nextInt(65) + 5);
            detail.setProcTime(random.nextInt(25) + 5);
            detail.setId(i);
            detail.setProduct_id(2);
            dao.createDetail(detail);
        }
        for (int i = 41; i < 61; i++) {
            Detail detail = new Detail();
            detail.setName("d" + i);
            detail.setAssemTime(random.nextInt(65) + 5);
            detail.setProcTime(random.nextInt(25) + 5);
            detail.setId(i);
            detail.setProduct_id(3);
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
