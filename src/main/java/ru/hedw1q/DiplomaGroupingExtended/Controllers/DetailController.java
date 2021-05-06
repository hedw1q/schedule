package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Machine;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Operation;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;

import java.net.URL;
import java.util.*;

/**
 * @author hedw1q
 */
public class DetailController implements Initializable {

    @FXML
    TextField detName;
    @FXML
    TextField assemTime;
    @FXML
    TextField detId;
    @FXML
    TextField route;
    @FXML
    TextField routeTimes;
    @FXML
    ComboBox<String> detailComboBox;
@FXML
    TableView<Detail> tableView;
@FXML
ComboBox<String> machineComboBox;
    public void createNewDetail(ActionEvent event) {
        DAO dao = DAO.getInstance();

        Detail detail = new Detail();
        detail.setName(detName.getText());
        detail.setAssemTime(Integer.parseInt(assemTime.getText()));
        detail.setId(Integer.parseInt(detId.getText()));

        String [] routes=route.getText().split(",");
        String [] routeTimess=routeTimes.getText().split(",");

        List<Operation> operationList=new LinkedList<>();
        for(int i=0;i<routes.length;i++){
            Operation operation=new Operation(new Machine(Integer.parseInt(routes[i])),Integer.parseInt(routeTimess[i]));
            operationList.add(operation);
        }

        detail.setOperations(operationList);

        dao.createDetail(detail);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Создание детали");
        alert.setContentText("Деталь успешно создана");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public void updateTable(){
        DAO dao = DAO.getInstance();

        TableColumn<Detail, Integer> idCol = new TableColumn<>("ID Детали");
        TableColumn<Detail, String> nameCol = new TableColumn<>("Наименование");
        TableColumn<Detail, Integer> product_idCol = new TableColumn<>("ID изделия");
        TableColumn<Detail, String> opCol = new TableColumn<>("Маршрут");
        TableColumn<Detail, String> opTimeCol = new TableColumn<>("Времена обработки согласно маршруту");


        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_idCol.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        opCol.setCellValueFactory(new PropertyValueFactory<>("route"));
        opTimeCol.setCellValueFactory(new PropertyValueFactory<>("routeTimes"));


        tableView.getColumns().addAll(idCol, nameCol, product_idCol, opCol, opTimeCol);
        tableView.setItems(FXCollections.observableList(dao.getAllDetails()));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAO dao=DAO.getInstance();
        updateTable();
        List<String> list = dao.getAllMachinesNames();
        machineComboBox.getItems().addAll(list);    }
}
