package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Lazy
@Service
public class NavigationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationService.class);

    private Stage stage;

    @Autowired
    private AuthService authService;

    public NavigationService(AuthService authService) {
        this.authService = authService;
    }

    public void navigateToSplash() {
        loadScene(ViewFxml.SPLASH_VIEW_FXML.getFxmlFile());
    }

    public void navigateToLogin() {
        if(authService.hasAnyUsers()) {
            navigateToRegister();
        } else {
            loadScene(ViewFxml.LOGIN_VIEW_FXML.getFxmlFile());
        }
    }

    public void navigateToRegister() {
        loadScene(ViewFxml.REGISTER_VIEW_FXML.getFxmlFile());
    }

    public void navigateToVerification() {
        loadScene(ViewFxml.VERIFY_VIEW_FXML.getFxmlFile());
    }

    public void navigateToMain() {
        loadScene(ViewFxml.MAIN_VIEW_FXML.getFxmlFile());
    }

    public void navigateToResetPassword() {
        loadScene(ViewFxml.RESET_PASSWORD_VIEW_FXML.getFxmlFile());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Fecha a janela atual sem encerrar a aplicação
     * @param node Qualquer node (componente) da janela atual
     */
    public void closeCurrentWindow(Node node) {
        if (node != null) {
            Stage currentStage = (Stage) node.getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * Fecha uma janela específica sem encerrar a aplicação
     * @param window A janela que deve ser fechada
     */
    public void closeWindow(Window window) {
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

    /**
     * Fecha a janela atual e abre uma nova
     * @param node Node da janela atual
     * @param fxml Arquivo FXML da nova janela
     */
    public void closeCurrentAndLoadNew(Node node, String fxml) {
        if (node != null) {
            Stage currentStage = (Stage) node.getScene().getWindow();
            currentStage.close();
            loadScene(fxml);
        }
    }

    private void loadScene(String fxml) {
        try {
            if(stage == null) {
                LOGGER.error("Stage is null. Make sure it's properly initialized.");
                throw new IllegalStateException("Stage not initialized");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setOnShown(e -> stage.centerOnScreen());
            stage.show();
        } catch (Exception e) {
            LOGGER.error("Error loading scene: {}", fxml, e);
            throw new IllegalStateException("Failed to load scene: " + fxml, e);
        }
    }


    public boolean shouldRedirectToRegister() {
        try {
            return !authService.hasAnyUsers();
        } catch (Exception e) {
            LOGGER.error("Error checking for users", e);
            return false;
        }
    }
}
