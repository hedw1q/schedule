package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DAO dao = DAO.getInstance();
        List<String> detailList = dao.getDetailList();
        detailPicker.getItems().addAll(detailList);
    }
}
