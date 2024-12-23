package br.com.rafaelvieira.cryptocypher.controller;

import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import br.com.rafaelvieira.cryptocypher.service.VerificationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationController {

    @FXML
    private TextField codeField;

    @FXML
    private Label messageLabel;

    @FXML
    private Label instructionLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private NavigationService navigationService;

    private String userEmail;

    private VerificationType verificationType;



    public void initialize(String email, VerificationType type) {
        this.userEmail = email;
        this.verificationType = type;
        updateInstructionLabel();
    }

    private void updateInstructionLabel() {
        String instruction = switch (verificationType) {
            case REGISTRATION -> "Please enter the verification code sent to your email to complete registration";
            case PASSWORD_RESET -> "Please enter the verification code sent to your email to reset your password";
            case FIRST_ACCESS -> "Please enter the verification code sent to your email for first access";
        };
        instructionLabel.setText(instruction);
    }

    @FXML
    private void handleVerification() {
        if (codeField.getText().isEmpty()) {
            showMessage("Please enter the verification code", true);
            return;
        }

        progressIndicator.setVisible(true);

        new Thread(() -> {
            try {
                boolean isValid = switch (verificationType) {
                    case REGISTRATION, FIRST_ACCESS ->
                            verificationService.verifyUser(userEmail, codeField.getText());
                    case PASSWORD_RESET ->
                            verificationService.verifyResetCode(userEmail, codeField.getText());
                };

                Platform.runLater(() -> {
                    if (isValid) {
                        onSuccessfulVerification();
                    } else {
                        showMessage("Invalid verification code", true);
                    }
                    progressIndicator.setVisible(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showMessage("Verification failed: " + e.getMessage(), true);
                    progressIndicator.setVisible(false);
                });
            }
        }).start();
    }

    @FXML
    private void handleResendCode() {
        progressIndicator.setVisible(true);

        new Thread(() -> {
            try {
                verificationService.resendVerificationCode(userEmail);
                Platform.runLater(() -> {
                    showMessage("New verification code sent to your email", false);
                    progressIndicator.setVisible(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showMessage("Failed to resend code: " + e.getMessage(), true);
                    progressIndicator.setVisible(false);
                });
            }
        }).start();
    }

    private void onSuccessfulVerification() {
        switch (verificationType) {
            case REGISTRATION -> {
                showMessage("Registration completed successfully!", false);
                navigationService.navigateToLogin();
            }
            case PASSWORD_RESET -> {
                showMessage("Code verified successfully!", false);
                navigationService.navigateToResetPassword();
            }
            case FIRST_ACCESS -> {
                verificationService.completeFirstAccess(userEmail);
                showMessage("First access verified successfully!", false);
                navigationService.navigateToMain();
            }
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public enum VerificationType {
        REGISTRATION,
        PASSWORD_RESET,
        FIRST_ACCESS
    }
}
