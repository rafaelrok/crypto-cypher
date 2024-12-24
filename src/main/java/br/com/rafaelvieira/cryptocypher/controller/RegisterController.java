package br.com.rafaelvieira.cryptocypher.controller;

import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.service.AuthService;
import br.com.rafaelvieira.cryptocypher.service.EmailService;
import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import br.com.rafaelvieira.cryptocypher.service.VerificationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class RegisterController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    private final UserRepository userRepository;
    private final AuthService authService;
    private final NavigationService navigationService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Autowired
    public RegisterController(UserRepository userRepository,
                              AuthService authService,
                              NavigationService navigationService,
                              EmailService emailService,
                              VerificationService verificationService) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.navigationService = navigationService;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @FXML
    private void handleRegister() {
        if (!validateFields()) {
            return;
        }

        UserRegister request = new UserRegister(
                usernameField.getText(),
                fullNameField.getText(),
                emailField.getText(),
                passwordField.getText()
        );

        try {
            authService.registerUser(request);
            messageLabel.setText("Registration successful! Please check your email for verification.");
            messageLabel.setStyle("-fx-text-fill: green;");
            final User user = userRepository.findByEmail(emailField.getText()).orElseThrow();
            if (!verificationService.isFirstAccess(user.getEmail())) {
                switchToLogin();
            } else {
                showVerificationScreen();
            }
        } catch (Exception e) {
            messageLabel.setText("Registration failed: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean validateFields() {
        if (usernameField.getText().isEmpty() ||
                fullNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {

            messageLabel.setText("All fields are required!");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            messageLabel.setText("Passwords do not match!");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        return true;
    }

    @FXML
    private void switchToLogin() {
        navigationService.navigateToLogin();
    }

    private void showVerificationScreen() {
        navigationService.navigateToVerification();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No seu RegisterViewController ou LoginViewController
    }
}

