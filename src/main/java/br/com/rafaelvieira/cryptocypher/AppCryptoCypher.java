package br.com.rafaelvieira.cryptocypher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppCryptoCypher extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppCryptoCypher.class.getResource("view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("crypto-cypher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
