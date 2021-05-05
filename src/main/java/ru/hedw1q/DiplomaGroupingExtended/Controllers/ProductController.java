package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author hedw1q
 */
public class ProductController implements Initializable {


    @FXML
    TextField textProductName;
    @FXML
    TextField textDetailList;
    @FXML
    ComboBox<String> detailPicker;
    @FXML
    TableView<Product> tableView;

    public void createNewProduct(ActionEvent event) {
        DAO dao = DAO.getInstance();

        Product product = new Product();


        List<Detail> detailList = new ArrayList<>();

        String[] stringNumbers = textDetailList.getText().split(",");

        for (String stringNumber : stringNumbers) {
            detailList.add(dao.getDetailById(Integer.parseInt(stringNumber)));
        }

        product.setName(textProductName.getText());
        product.setDetails(detailList);

        dao.createProduct(product);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Создание изделия");
        alert.setContentText("Изделие успешно создано");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void addDetail(ActionEvent event) {
        DAO dao = DAO.getInstance();
        Detail detail = dao.getDetailByName(detailPicker.getValue());
        String previousText = textDetailList.getText();
        textDetailList.setText(previousText + detail.getId() + ",");
    }

    public void updateTable() {
        DAO dao = DAO.getInstance();
        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        TableColumn<Product, String> colName = new TableColumn<>("Наименование");
        TableColumn<Product, String> colDet = new TableColumn<>("Детали, входящие в данное изделие");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colDet.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDetails().toString());
            }
        });
        colDet.getStyleClass().add("table-view");

        tableView.getColumns().addAll(colId, colName, colDet);
        tableView.setItems(FXCollections.observableList(dao.getProductList()));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAO dao = DAO.getInstance();
        List<String> detailList = dao.getDetailList();
        detailPicker.getItems().addAll(detailList);
        updateTable();

    }
}
