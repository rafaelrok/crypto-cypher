package br.com.rafaelvieira.cryptocypher.config;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class StageManager {
    private final SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;

    public StageManager(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    public void switchScene(String fxmlPath) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(fxmlPath);
        show(viewRootNodeHierarchy);
    }

    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootNode;
    }

    private void show(Parent rootNode) {
        Scene scene = prepareScene(rootNode);

        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Crypto Cypher");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Scene prepareScene(Parent rootNode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootNode);
        }
        scene.setRoot(rootNode);
        return scene;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void displayInitialScene() {
        loadViewNodeHierarchy("/fxml/main.fxml");
    }
}
