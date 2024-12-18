package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class StageManager {

    private static final Logger log = LoggerFactory.getLogger(StageManager.class);

    private final SpringFXMLLoader springFXMLLoader;
    private final Stage primaryStage;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage primaryStage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = primaryStage;
    }

    public void switchScene(final ViewFxml view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }

    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
        } catch (Exception e) {
            logAndExit("Unable to load FXML file: " + fxmlFilePath, e);
        }
        return rootNode;
    }

    private void show(Parent rootNode, String title) {
        Scene scene = prepareScene(rootNode);

        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Crypto Cipher");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception e) {
            logAndExit("Unable to show scene for title" + primaryStage.getTitle(), e);
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

    private void logAndExit(String errorMsg, Exception exception) {
        log.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }
}
