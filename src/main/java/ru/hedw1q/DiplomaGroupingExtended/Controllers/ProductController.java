package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Product;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hedw1q
 */
public class ProductController {


    @FXML
    TextField textProductName;
    @FXML
    TextField textDetailList;

    public void createNewProduct(ActionEvent event) {
        DAO dao=DAO.getInstance();

        Product product = new Product();


        List<Detail> detailList=new ArrayList<>();

        String[] stringNumbers=textDetailList.getText().split(",");

        for(String stringNumber:stringNumbers){
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


}
