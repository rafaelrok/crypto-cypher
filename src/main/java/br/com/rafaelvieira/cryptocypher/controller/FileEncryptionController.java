package br.com.rafaelvieira.cryptocypher.controller;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import br.com.rafaelvieira.cryptocypher.enums.ExtensionFile;
import br.com.rafaelvieira.cryptocypher.service.EncryptionService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;


@Controller
public class FileEncryptionController implements Initializable {

    public final static String ENCRYPT = "encrypt";
    public final static String DECRYPT = "decrypt";

    @FXML
    private AnchorPane main_form;

    @FXML
    private Circle top_profile;

    @FXML
    private Label top_username;

    @FXML
    private Label date_time;

    @FXML
    private Label current_form;

    @FXML
    private Label nav_adminID;

    @FXML
    private Label nav_username;

    @FXML
    private Button dashboard_btn;


    @FXML
    private Button profile_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_AD;

    @FXML
    private Label dashboard_TP;

    @FXML
    private Label dashboard_AP;

    @FXML
    private Label dashboard_tA;

    @FXML
    private AreaChart<?, ?> dashboad_chart_PD;

    @FXML
    private BarChart<?, ?> dashboad_chart_DD;

    @FXML
    private AnchorPane profile_form;

    @FXML
    private Circle profile_circle;

    @FXML
    private Button profile_importBtn;

    @FXML
    private Label profile_label_adminIO;

    @FXML
    private Label profile_label_name;

    @FXML
    private Label profile_label_email;

    @FXML
    private Label profile_label_dateCreated;

    @FXML
    private TextField profile_adminID;

    @FXML
    private TextField profile_username;

    @FXML
    private TextField profile_email;

    @FXML
    private ComboBox<String> profile_status;

    @FXML
    private Button profile_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField inputTextArea;

    @FXML
    private ComboBox<CryptographyType> algorithmComboBox;

    private File selectedFile;

    private final EncryptionService encryptionService;

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    @Autowired
    public FileEncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @FXML
    public void handleEncryptFileUpload() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Supported Files",
                ExtensionFile.TEXT.getExtension(), ExtensionFile.CSV.getExtension());
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            inputTextArea.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    public void handleEncryptFile() {
        if (algorithmComboBox.getValue() == null) {
            showAlert("Por favor, selecione um algoritmo de criptografia.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedFile != null) {
            new Thread(() -> processFile(selectedFile, ENCRYPT)).start();
        } else {
            String text = inputTextArea.getText().trim();
            if (!text.isEmpty()) {
                new Thread(() -> processText(text, ENCRYPT)).start();
            } else {
                showAlert("Nenhum arquivo selecionado ou texto inserido para criptografia.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    public void handleDecryptFile() {
        if (algorithmComboBox.getValue() == null) {
            showAlert("Por favor, selecione um algoritmo de criptografia.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedFile != null) {
            new Thread(() -> processFile(selectedFile, DECRYPT)).start();
        } else {
            String text = inputTextArea.getText().trim();
            if (!text.isEmpty()) {
                new Thread(() -> processText(text, DECRYPT)).start();
            } else {
                showAlert("Nenhum arquivo selecionado ou texto inserido para descriptografia.", Alert.AlertType.WARNING);
            }
        }
    }

    private void processFile(File file, String action) {
        try {
            Platform.runLater(() -> progressBar.setProgress(0));
            String content = new String(Files.readAllBytes(file.toPath()));
            processText(content, action);
        } catch (IOException e) {
            handleError("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private void processText(String content, String action) {
        try {
            if (content == null || content.isEmpty()) {
                handleError("O texto de entrada não pode estar vazio");
                return;
            }

            Platform.runLater(() -> progressBar.setProgress(0));

            int totalLength = content.length();
            int chunkSize = Math.max(1, totalLength / 100); // Evita divisão por zero
            StringBuilder resultContent = new StringBuilder();

            for (int i = 0; i < Math.ceil((double) totalLength / chunkSize); i++) {
                int start = i * chunkSize;
                int end = Math.min((i + 1) * chunkSize, totalLength);
                String chunk = content.substring(start, end);

                // Concatena os pedaços e processa tudo após o loop
                resultContent.append(chunk);

                final double progress = (double) (i + 1) / Math.ceil((double) totalLength / chunkSize);
                Platform.runLater(() -> progressBar.setProgress(progress));

                // Pequena pausa para visualização do progresso
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            String finalResult;
            try {
                if (ENCRYPT.equals(action)) {
                    finalResult = encryptionService.encrypt(resultContent.toString());
                } else {
                    finalResult = encryptionService.decrypt(resultContent.toString());
                }
            } catch (Exception e) {
                handleError("Erro ao " + action + " o conteúdo: " + e.getMessage());
                return;
            }

            Platform.runLater(() -> {
                progressBar.setProgress(1);
                outputTextArea.setText(finalResult);
                showAlert("Processo concluído com sucesso!", Alert.AlertType.INFORMATION);
            });

        } catch (Exception e) {
            handleError("Erro inesperado: " + e.getMessage());
        }
    }

    private void handleError(String errorMessage) {
        Platform.runLater(() -> {
            progressBar.setProgress(0);
            showAlert(errorMessage, Alert.AlertType.ERROR);
        });
    }

    @FXML
    public void handleCopyOutput() {
        String output = outputTextArea.getText();
        if (output.isEmpty()) {
            showAlert("Não há conteúdo para copiar.", Alert.AlertType.WARNING);
            return;
        }

        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(output);
        clipboard.setContent(content);

        showAlert("Conteúdo copiado para a área de transferência.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void handleExportOutput() {
        if (outputTextArea.getText().isEmpty()) {
            showAlert("Não há conteúdo para exportar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Resultado");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt")
        );
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                encryptionService.exportOutput(file, outputTextArea.getText());
                showAlert("Arquivo exportado com sucesso!", Alert.AlertType.INFORMATION);
                handleCleanAllInputs();
            } catch (IOException e) {
                showAlert("Erro ao exportar arquivo: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void handleCleanAllInputs() {
        inputTextArea.clear();
        outputTextArea.clear();
        selectedFile = null;
        algorithmComboBox.getSelectionModel().selectFirst();
        progressBar.setProgress(0);
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType, message, ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle(alertType == Alert.AlertType.INFORMATION ? "Sucesso" :
                    alertType == Alert.AlertType.WARNING ? "Aviso" : "Erro");
            alert.showAndWait();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        algorithmComboBox.getItems().addAll(CryptographyType.values());
        algorithmComboBox.getSelectionModel().selectFirst();

        algorithmComboBox.setOnAction(event -> {
            CryptographyType selectedType = algorithmComboBox.getValue();
            if (selectedType != null) {
                System.out.println("Mudando para algoritmo: " + selectedType.name());
                encryptionService.setCryptographyType(selectedType);
            }
        });

        algorithmComboBox.setConverter(new StringConverter<CryptographyType>() {
            @Override
            public String toString(CryptographyType type) {
                return type != null ? type.name() : "";
            }

            @Override
            public CryptographyType fromString(String string) {
                return string != null ? CryptographyType.valueOf(string) : null;
            }
        });
    }
}
