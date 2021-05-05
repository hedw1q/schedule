package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.hedw1q.DiplomaGroupingExtended.Entity.*;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;
import ru.hedw1q.DiplomaGroupingExtended.Service.GanttChart;
import ru.hedw1q.DiplomaGroupingExtended.Service.Planner;
import ru.hedw1q.DiplomaGroupingExtended.Service.ScheduleHandler;

import java.net.URL;
import java.util.*;

import static ru.hedw1q.DiplomaGroupingExtended.Service.GanttChart.getCategoryDataset;

public class MainController implements Initializable {


    @FXML
    ComboBox<String> orderComboBox;

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
    public void redirectToOrder(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Order.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Создание нового заказа");
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
    public void planOrder(ActionEvent event) throws Exception {
        DAO dao = DAO.getInstance();
        ScheduleHandler scheduleHandler = ScheduleHandler.getInstance();


        LinkedList<Detail> detailList = new LinkedList<>();
        Order order=dao.getOrderByName(orderComboBox.getValue());

        int productCount=order.getProducts().size();

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
        System.out.println(groupMap.get(groupMap.size()));
        new Planner().planOrder(new GanttChart("Диаграмма Гантта").getGanttChartTab(getCategoryDataset(groupMap, copyDetailList)),
                createInfoTable(copyDetailList));

    }


    public static Tab createInfoTable(List<Detail> detailList) {

        TableView<Detail> tableView = new TableView();

        TableColumn<Detail, Integer> idCol = new TableColumn<>("ID Детали");
        TableColumn<Detail, String> nameCol = new TableColumn<>("Наименование");
        TableColumn<Detail, Integer> product_idCol = new TableColumn<>("ID изделия");
        TableColumn<Detail, String> opCol = new TableColumn<>("Маршрут");
        TableColumn<Detail, String> opTimeCol = new TableColumn<>("Времена обработки согласно маршруту");
        TableColumn<Detail, Date> procStartCol = new TableColumn<>("Начало обработки");
        TableColumn<Detail, Date> procEndCol = new TableColumn<>("Конец обработки");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_idCol.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        opCol.setCellValueFactory(new PropertyValueFactory<>("route"));
        opTimeCol.setCellValueFactory(new PropertyValueFactory<>("routeTimes"));
        procStartCol.setCellValueFactory(new PropertyValueFactory<>("procStart"));
        procEndCol.setCellValueFactory(new PropertyValueFactory<>("procEnd"));

        tableView.getColumns().addAll(idCol, nameCol, product_idCol, opCol, opTimeCol, procStartCol, procEndCol);
        tableView.setItems(FXCollections.observableList(detailList));
        Tab tab1 = new Tab("Информация о деталях");
        tab1.setContent(tableView);

        return tab1;
    }

    public void createRandomDetails(ActionEvent event) {
        Random random = new Random();
        DAO dao = DAO.getInstance();
        dao.deleteAllDetails();
        for (int i = 1; i < 61; i++) {
            Detail detail = new Detail();

            if (i >= 1 && i <= 20) detail.setProduct_id(1);
            if (i >= 21 && i <= 40) detail.setProduct_id(2);
            if (i >= 41 && i <= 60) detail.setProduct_id(3);

            detail.setName("Деталь №" + i);
            detail.setAssemTime(random.nextInt(60) + 25);
            detail.setOperations(generateRandomRoute());
            detail.setId(i);
            dao.createDetail(detail);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Тестовые данные успешно созданы");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static List<Operation> generateRandomRoute() {
        Random rand = new Random();
        List<Operation> operations = new LinkedList<>();
        List<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Collections.shuffle(list);
        for (int i = 0; i < 5; i++) {

            Machine machine = new Machine(list.get(0));
            list.remove(0);
            Operation operation = new Operation(machine, rand.nextInt(5) + 2);
            operations.add(operation);
        }
        return operations;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAO dao = DAO.getInstance();
        List<String> orderList = dao.getOrderList();
        orderComboBox.getItems().addAll(orderList);
    }
}
