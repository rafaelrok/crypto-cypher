package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Setter
@Service
public class NavigationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationService.class);

    private Stage stage;

    public void navigateToSplash() {
        loadScene(ViewFxml.SPLASH_VIEW_FXML.getFxmlFile());
    }

    public void navigateToLogin() {
        loadScene(ViewFxml.LOGIN_VIEW_FXML.getFxmlFile());
//        loadScene("login-view.fxml");
    }

    public void navigateToRegister() {
        loadScene(ViewFxml.REGISTER_VIEW_FXML.getFxmlFile());
//        loadScene("register-view.fxml");
    }

    public void navigateToVerification() {
        loadScene(ViewFxml.VERIFY_VIEW_FXML.getFxmlFile());
//        loadScene("verification-view.fxml");
    }

    public void navigateToMain() {
        loadScene(ViewFxml.MAIN_VIEW_FXML.getFxmlFile());
//        loadScene("main-view.fxml");
    }

    public void navigateToResetPassword() {
        loadScene(ViewFxml.RESET_PASSWORD_VIEW_FXML.getFxmlFile());
//        loadScene("reset-password-view.fxml");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            LOGGER.error("Error loading scene: {}", fxml, e);
        }
    }


}
