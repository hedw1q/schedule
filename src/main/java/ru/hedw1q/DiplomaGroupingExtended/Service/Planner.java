package ru.hedw1q.DiplomaGroupingExtended.Service;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * @author hedw1q
 */
public class Planner {
    public void planOrder(Tab ... tabs){
        Stage stage = new Stage();
        stage.setTitle("Планирование заказа");
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(tabs);

        Scene scene = new Scene(tabPane,1600, 300);
        stage.setScene(scene);
        stage.show();
    }
}
