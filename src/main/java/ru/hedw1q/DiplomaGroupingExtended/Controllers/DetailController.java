package ru.hedw1q.DiplomaGroupingExtended.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.hedw1q.DiplomaGroupingExtended.Entity.Detail;
import ru.hedw1q.DiplomaGroupingExtended.Service.DAO;

import java.util.Random;

/**
 * @author hedw1q
 */
public class DetailController {

    @FXML
    TextField detName;
    @FXML
    TextField procTime;
    @FXML
    TextField assemTime;
    @FXML
    TextField detId;
    @FXML
    ComboBox<String> detailComboBox;

    public void createNewDetail(ActionEvent event) {
        DAO dao = DAO.getInstance();

        Detail detail = new Detail();
        detail.setName(detName.getText());
        detail.setAssemTime(Integer.parseInt(assemTime.getText()));
        detail.setProcTime(Integer.parseInt(procTime.getText()));
        detail.setId(Integer.parseInt(detId.getText()));
        dao.createDetail(detail);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Создание детали");
        alert.setContentText("Деталь успешно создана");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void getRandomNumbers(ActionEvent event){
        Random random=new Random();
        procTime.setText(String.valueOf(random.nextInt(25)+5));
        assemTime.setText(String.valueOf(random.nextInt(25)+5));
    }


}
