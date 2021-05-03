package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Order;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hedw1q
 */
public class OrderController {
    @FXML
    TextField textOrderName;
@FXML
    TextField textProductList;
    public void createNewOrder(ActionEvent event) {
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
}
