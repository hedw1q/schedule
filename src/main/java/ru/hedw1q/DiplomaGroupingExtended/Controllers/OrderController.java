package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Order;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author hedw1q
 */
public class OrderController  implements Initializable {
    @FXML
    TextField textOrderName;
@FXML
    TextField textProductList;
@FXML
    ComboBox<String> productPicker;
@FXML
    TableView<Order> tableView;
    public void createNewOrder(ActionEvent event)  {
        DAO dao=DAO.getInstance();

        Order order=new Order();


        List<Product> products=new ArrayList<>();

        String[] stringNumbers=textProductList.getText().split(",");

        for(String stringNumber:stringNumbers){
            products.add(dao.getProductById(Integer.parseInt(stringNumber)));
        }

        order.setName(textOrderName.getText());
        order.setProducts(products);

        dao.createOrder(order);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Создание изделия");
        alert.setContentText("Заказ успешно создан");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void addProduct(ActionEvent event){
        DAO dao=DAO.getInstance();
        Product product=dao.getProductByName(productPicker.getValue());
        String previousText=textProductList.getText();
        textProductList.setText(previousText+product.getId()+",");
    }

    public void updateTable() {
        DAO dao = DAO.getInstance();
        TableColumn<Order, Integer> colId = new TableColumn<>("ID");
        TableColumn<Order, String> colName = new TableColumn<>("Наименование");
        TableColumn<Order, String> colProd = new TableColumn<>("Изделия, входящие в заказ");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getProducts().toString());
            }
        });
        colProd.getStyleClass().add("table-view");

        tableView.getColumns().addAll(colId, colName, colProd);
        tableView.setItems(FXCollections.observableList(dao.getAllOrders()));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAO dao = DAO.getInstance();
        List<String> productList = dao.getProductNamesList();
        productPicker.getItems().addAll(productList);
        updateTable();
    }
}
