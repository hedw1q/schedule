package ru.hedw1q.DiplomaGroupingExtended;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        stage.setTitle("Группирование деталей для стапельной сборки");
        stage.setScene(new Scene(root));
        stage.show();
    }
    @SuppressWarnings("")
    public static void main(String[] args) { launch(args); }
}
