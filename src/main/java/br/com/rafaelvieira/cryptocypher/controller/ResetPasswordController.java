package br.com.rafaelvieira.cryptocypher.controller;

import br.com.rafaelvieira.cryptocypher.service.AuthService;
import br.com.rafaelvieira.cryptocypher.service.EmailService;
import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import br.com.rafaelvieira.cryptocypher.service.VerificationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ResetPasswordController implements Initializable {

    @FXML
    private TextField reset_email;
    @FXML
    private TextField reset_verification_code;
    @FXML
    private PasswordField reset_new_password;
    @FXML
    private PasswordField reset_confirm_password;
    @FXML
    private Button reset_send_code_btn;
    @FXML
    private Button reset_verify_code_btn;
    @FXML
    private Button reset_password_btn;
    @FXML
    private Hyperlink reset_back_to_login;

    private final EmailService emailService;
    private final VerificationService verificationService;
    private final AuthService authService;
    private final NavigationService navigationService;

    @Autowired
    public ResetPasswordController(EmailService emailService,
                                   VerificationService verificationService,
                                   AuthService authService,
                                   NavigationService navigationService) {
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.authService = authService;
        this.navigationService = navigationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButtonActions();
        setupNavigationEvents();
    }

    private void setupButtonActions() {
        reset_send_code_btn.setOnAction(event -> handleSendResetCode());
        reset_verify_code_btn.setOnAction(event -> handleVerifyResetCode());
        reset_password_btn.setOnAction(event -> handleResetPassword());
    }

    private void setupNavigationEvents() {
        reset_back_to_login.setOnAction(event ->
                navigationService.navigateToLogin());
    }

    private void handleSendResetCode() {
        String email = reset_email.getText().trim();
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, insira seu email.");
            return;
        }

        try {
            emailService.sendPasswordResetEmail(email, verificationService.generateResetCode(email));

            reset_verification_code.setVisible(true);
            reset_send_code_btn.setVisible(false);
            reset_verify_code_btn.setVisible(true);

            showAlert(Alert.AlertType.INFORMATION,
                    "Código Enviado",
                    "Um código de verificação foi enviado para seu email.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro",
                    "Erro ao enviar código: " + e.getMessage());
        }
    }

    private void handleVerifyResetCode() {
        String email = reset_email.getText().trim();
        String code = reset_verification_code.getText().trim();
        if (code.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, insira o código de verificação.");
            return;
        }

        try {
            if (verificationService.verifyResetCode(email, code)) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Código Válido",
                        "Código de verificação válido.");
                reset_new_password.setVisible(true);
                reset_confirm_password.setVisible(true);
                reset_verify_code_btn.setVisible(false);
                reset_password_btn.setVisible(true);
            } else {
                showAlert(Alert.AlertType.ERROR,
                        "Código Inválido",
                        "Código de verificação inválido.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro",
                    "Erro ao verificar código: " + e.getMessage());
        }
    }

    private void handleResetPassword() {
        String email = reset_email.getText().trim();
        String newPassword = reset_new_password.getText().trim();
        String confirmPassword = reset_confirm_password.getText().trim();
        String code = reset_verification_code.getText().trim();
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, insira a nova senha.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Senhas Diferentes", "As senhas não coincidem.");
            return;
        }

        try {
            authService.resetPassword(email, code, newPassword);
            showAlert(Alert.AlertType.INFORMATION,
                    "Senha Alterada",
                    "Senha alterada com sucesso.");
            navigationService.navigateToLogin();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro",
                    "Erro ao alterar senha: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
