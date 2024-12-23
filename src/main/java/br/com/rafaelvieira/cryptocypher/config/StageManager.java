package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@Lazy
public class StageManager {

    private static final Logger log = LoggerFactory.getLogger(StageManager.class);

    private final NavigationService navigationService;
    private final SpringFXMLLoader springFXMLLoader;
    private final Stage primaryStage;

    @Autowired
    public StageManager(SpringFXMLLoader springFXMLLoader,
                        @Qualifier("primaryStage")Stage primaryStage,
                        NavigationService navigationService) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = primaryStage;
        this.navigationService = navigationService;
    }

    public void switchScene(final ViewFxml view) {
        log.debug("Switching to scene: {}", view.name());
        navigationService.setStage(primaryStage);

        if (view == ViewFxml.LOGIN_VIEW_FXML) {
            log.debug("Checking if should redirect to register");
            if (navigationService.shouldRedirectToRegister()) {
                log.debug("Redirecting to register view");
                switchScene(ViewFxml.REGISTER_VIEW_FXML);
                return;
            }
        }

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
