package br.com.rafaelvieira.cryptocypher.controller;

import br.com.rafaelvieira.cryptocypher.payload.request.UserAuthentication;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.service.AuthService;
import br.com.rafaelvieira.cryptocypher.service.EmailService;
import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import br.com.rafaelvieira.cryptocypher.service.VerificationService;
import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class AuthController implements Initializable {

    // Forms elements
    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane login_form;
    @FXML
    private AnchorPane register_form;
    @FXML
    private AnchorPane verify_form;
    @FXML
    private AnchorPane reset_password_form;

    // Login Form elements
    @FXML
    private TextField login_username;
    @FXML
    private PasswordField login_password;
    @FXML
    private TextField login_showPassword;
    @FXML
    private CheckBox login_checkBox;
    @FXML
    private Button login_loginBtn;
    @FXML
    private Hyperlink login_registerHere;

    // Elements for password reset
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
    @FXML
    private Hyperlink login_forgotPassword;

    // Register Form elements
    @FXML
    private TextField register_email;
    @FXML
    private TextField register_username;
    @FXML
    private TextField register_fullname;
    @FXML
    private PasswordField register_password;
    @FXML
    private TextField register_showPassword;
    @FXML
    private CheckBox register_checkBox;
    @FXML
    private Button register_signupBtn;
    @FXML
    private Hyperlink register_loginHere;

    //Elementos para a verificaçao
    @FXML
    private TextField verify_code;
    @FXML
    private Button verify_verifyBtn;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private NavigationService navigationService;


    @FXML
    public void initialize() {
        setupPasswordVisibility();
        setupNavigationEvents();
        setupButtonActions();
        setupNavigationRegister();
        setupNavigationVerify();
        setupNavigationResetPassword();
    }

    private void setupPasswordVisibility() {
        // Login form password visibility
        login_checkBox.setOnAction(event -> {
            if (login_checkBox.isSelected()) {
                login_showPassword.setText(login_password.getText());
                login_showPassword.setVisible(true);
                login_password.setVisible(false);
            } else {
                login_password.setText(login_showPassword.getText());
                login_showPassword.setVisible(false);
                login_password.setVisible(true);
            }
        });

        // Register form password visibility
        register_checkBox.setOnAction(event -> {
            if (register_checkBox.isSelected()) {
                register_showPassword.setText(register_password.getText());
                register_showPassword.setVisible(true);
                register_password.setVisible(false);
            } else {
                register_password.setText(register_showPassword.getText());
                register_showPassword.setVisible(false);
                register_password.setVisible(true);
            }
        });
    }

    private void setupNavigationEvents() {
        login_form.setVisible(true);
        login_registerHere.setOnAction(event -> {
            login_form.setVisible(false);
            navigationService.navigateToRegister();
            switchToLogin();
            register_form.setVisible(true);
        });

        register_loginHere.setOnAction(event -> {
            register_form.setVisible(false);
            login_form.setVisible(true);
        });
    }

    private void setupNavigationRegister() {
        login_registerHere.setOnAction(event -> {
            login_form.setVisible(false);
            register_form.setVisible(true);
            navigationService.navigateToRegister();
        });
    }

    private void setupNavigationVerify() {
        login_registerHere.setOnAction(event -> {
            login_form.setVisible(false);
            verify_form.setVisible(true);
            navigationService.navigateToVerification();
        });
    }

    private void setupNavigationResetPassword() {
        login_forgotPassword.setOnAction(event -> {
            login_form.setVisible(false);
            reset_password_form.setVisible(true);
            navigationService.navigateToResetPassword();
        });

        reset_back_to_login.setOnAction(event -> {
            reset_password_form.setVisible(false);
            login_form.setVisible(true);
        });
    }

    private void setupButtonActions() {
        login_loginBtn.setOnAction(event -> handleLogin());
        register_signupBtn.setOnAction(event -> handleRegister());
        reset_send_code_btn.setOnAction(event -> handleSendResetCode());
        reset_verify_code_btn.setOnAction(event -> handleVerifyResetCode());
        reset_password_btn.setOnAction(event -> handleResetPassword());
        verify_verifyBtn.setOnAction(event -> handleVerify());
    }

    private void handleLogin() {
        try {
            String username = login_username.getText();
            String password = login_password.isVisible() ?
                    login_password.getText() :
                    login_showPassword.getText();

            UserAuthentication loginRequest = new UserAuthentication(username, password);

//            JwtResponse response = authService.authenticateUser(loginRequest);
            AuthService.UserSession response = authService.authenticateUser(username, password);

            // Se autenticação bem sucedida
            if (response.isFirstAccess()) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Primeiro Acesso",
                        "Por favor, verifique seu email para completar o primeiro acesso.");
                // Aqui você pode navegar para a tela de verificação
            } else {
                // Navegar para a tela principal
//                 stageManager.switchScene(ViewFxml.MAIN_VIEW_FXML);
                navigationService.navigateToMain();
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro de Login",
                    "Erro ao fazer login: " + e.getMessage());
        }
    }

    private void handleSendResetCode() {
        String email = reset_email.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, insira seu email.");
            return;
        }

        try {
            authService.initiatePasswordReset(email);

            // Mostrar campo de código de verificação
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
        String code = reset_verification_code.getText().trim();
        String email = reset_email.getText().trim();

        if (code.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "Campo Vazio",
                    "Por favor, insira o código de verificação.");
            return;
        }

        try {
            if (verificationService.verifyResetCode(email, code)) {
                // Mostrar campos de nova senha
                reset_verification_code.setVisible(false);
                reset_verify_code_btn.setVisible(false);
                reset_new_password.setVisible(true);
                reset_confirm_password.setVisible(true);
                reset_password_btn.setVisible(true);
            } else {
                showAlert(Alert.AlertType.ERROR,
                        "Código Inválido",
                        "O código de verificação é inválido.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro",
                    "Erro ao verificar código: " + e.getMessage());
        }
    }

    private void handleResetPassword() {
        String newPassword = reset_new_password.getText();
        String confirmPassword = reset_confirm_password.getText();
        String email = reset_email.getText().trim();
        String code = reset_verification_code.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "Campos Vazios",
                    "Por favor, preencha todos os campos.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING,
                    "Senhas Diferentes",
                    "As senhas não coincidem.");
            return;
        }

        try {
            authService.resetPassword(email, code, newPassword);
            showAlert(Alert.AlertType.INFORMATION,
                    "Senha Alterada",
                    "Sua senha foi alterada com sucesso.");

            // Voltar para tela de login
            clearResetPasswordFields();
            reset_password_form.setVisible(false);
            login_form.setVisible(true);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro",
                    "Erro ao redefinir senha: " + e.getMessage());
        }
    }

    private void handleRegister() {
        try {
            String username = register_username.getText();
            String fullName = register_fullname.getText();
            String email = register_email.getText();
            String password = register_password.isVisible() ?
                    register_password.getText() :
                    register_showPassword.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING,
                        "Campos Vazios",
                        "Por favor, preencha todos os campos.");
                return;
            }

            UserRegister registerRequest = new UserRegister(
                    username,
                    fullName,
                    email,
                    password
            );

            authService.registerUser(registerRequest);

            showAlert(Alert.AlertType.INFORMATION,
                    "Registro Bem-sucedido",
                    "Por favor, verifique seu email para ativar sua conta.");

            // Limpar campos e voltar para tela de login
            clearRegisterFields();
            register_form.setVisible(false);
            login_form.setVisible(true);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro no Registro",
                    "Erro ao registrar: " + e.getMessage());
        }
    }

    private void handleVerify() {
        try {
            String email = reset_email.getText().trim();
            String code = verify_code.getText().trim();

            if (code.isEmpty()) {
                showAlert(Alert.AlertType.WARNING,
                        "Campo Vazio",
                        "Por favor, insira o código de verificação.");
                return;
            }

            if (verificationService.verifyUser(email, code)) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Verificação Bem-sucedida",
                        "Sua conta foi verificada com sucesso.");

                // Limpar campos e voltar para tela de login
                verify_code.clear();
                verify_form.setVisible(false);
                login_form.setVisible(true);

            } else {
                showAlert(Alert.AlertType.ERROR,
                        "Código Inválido",
                        "O código de verificação é inválido.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erro na Verificação",
                    "Erro ao verificar: " + e.getMessage());
        }
    }

    @FXML
    private void switchToLogin() {
        navigationService.closeCurrentAndLoadNew(register_form, ViewFxml.LOGIN_VIEW_FXML.getFxmlFile());
    }

    private void clearResetPasswordFields() {
        reset_email.clear();
        reset_verification_code.clear();
        reset_new_password.clear();
        reset_confirm_password.clear();

        // Resetar visibilidade dos componentes
        reset_verification_code.setVisible(false);
        reset_new_password.setVisible(false);
        reset_confirm_password.setVisible(false);
        reset_send_code_btn.setVisible(true);
        reset_verify_code_btn.setVisible(false);
        reset_password_btn.setVisible(false);
    }

    private void clearRegisterFields() {
        register_username.clear();
        register_email.clear();
        register_password.clear();
        register_showPassword.clear();
        register_checkBox.setSelected(false);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);


        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.centerOnScreen();

        alert.initModality(Modality.APPLICATION_MODAL);

        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);


        Platform.runLater(() -> {
            stage.requestFocus();
            alert.getDialogPane().getButtonTypes().stream()
                    .map(alert.getDialogPane()::lookupButton)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(Node::requestFocus);
        });

        alert.showAndWait();
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
        initialize();
    }
}

//package br.com.rafaelvieira.cryptocypher.controller;
//
//
//import br.com.rafaelvieira.cryptocypher.model.user.User;
//import br.com.rafaelvieira.cryptocypher.payload.request.UserAuthentication;
//import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
//import br.com.rafaelvieira.cryptocypher.payload.response.JwtResponse;
//import br.com.rafaelvieira.cryptocypher.payload.response.MessageResponse;
//import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
//import br.com.rafaelvieira.cryptocypher.security.jwt.JwtUtils;
//import br.com.rafaelvieira.cryptocypher.service.AuthService;
//import br.com.rafaelvieira.cryptocypher.service.EmailService;
//import br.com.rafaelvieira.cryptocypher.service.VerificationService;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Random;
//
//@Controller
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtils jwtUtils;
//    private final EmailService emailService;
//    private final VerificationService verificationService;
//    private final AuthService authService;
//
//    public AuthController(AuthenticationManager authenticationManager,
//                          UserRepository userRepository,
//                          PasswordEncoder passwordEncoder,
//                          JwtUtils jwtUtils,
//                          EmailService emailService,
//                          VerificationService verificationService, AuthService authService) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtUtils = jwtUtils;
//        this.emailService = emailService;
//        this.verificationService = verificationService;
//        this.authService = authService;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegister registerRequest) {
//        if (userRepository.existsByUsername(registerRequest.getUsername())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(registerRequest.getEmail())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        // Create verification code
//        String verificationCode = generateVerificationCode();
//
//        // Create new user
//        User user = User.builder()
//                .username(registerRequest.getUsername())
//                .email(registerRequest.getEmail())
//                .password(passwordEncoder.encode(registerRequest.getPassword()))
//                .fullName(registerRequest.getFullName())
//                .isVerified(false)
//                .isFirstAccess(true)
//                .verificationCode(verificationCode)
//                .build();
//
//        userRepository.save(user);
//
//        // Send verification email
//        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for verification."));
//    }
//
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserAuthentication loginRequest) {
//        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, jwtResponse.getTokenCookie().toString())
//                .body(jwtResponse);
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyUser(@RequestBody String code) {
//        return verificationService.verifyUser(code)
//                ? ResponseEntity.ok(new MessageResponse("User verified successfully!"))
//                : ResponseEntity.badRequest().body(new MessageResponse("Invalid verification code!"));
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String resetCode = generateVerificationCode();
//        user.setVerificationCode(resetCode);
//        userRepository.save(user);
//
//        emailService.sendPasswordResetEmail(email, resetCode);
//
//        return ResponseEntity.ok(new MessageResponse("Password reset code sent to your email!"));
//    }
//
//    @PostMapping("/verify-reset")
//    public ResponseEntity<?> verifyResetCode(@RequestBody String code) {
//        return verificationService.verifyResetCode(code)
//                ? ResponseEntity.ok(new MessageResponse("Code verified successfully!"))
//                : ResponseEntity.badRequest().body(new MessageResponse("Invalid reset code!"));
//    }
//
//    private String generateVerificationCode() {
//        Random random = new Random();
//        StringBuilder code = new StringBuilder();
//        for (int i = 0; i < 8; i++) {
//            code.append(random.nextInt(10));
//        }
//        return code.toString();
//    }
//}
