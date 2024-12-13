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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;


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

//    @FXML
//    private ComboBox<String> algorithmComboBox;
    @FXML
    private ComboBox<CryptographyType> algorithmComboBox;

    private File selectedFile;

    private EncryptionService encryptionService;

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
            Platform.runLater(() -> progressBar.setProgress(0));
            int totalLength = content.length();
            int chunkSize = Math.max(1, totalLength / 100); // Evita divisão por zero
            StringBuilder resultContent = new StringBuilder();

            for (int i = 0; i < Math.ceil((double) totalLength / chunkSize); i++) {
                int start = i * chunkSize;
                int end = Math.min((i + 1) * chunkSize, totalLength);
                String chunk = content.substring(start, end);

                String processedChunk;
                try {
                    if (ENCRYPT.equals(action)) {
                        processedChunk = encryptionService.encrypt(chunk);
                    } else {
                        processedChunk = encryptionService.decrypt(chunk);
                    }
                    resultContent.append(processedChunk);
                } catch (Exception e) {
                    handleError("Erro ao " + action + " o conteúdo: " + e.getMessage());
                    return;
                }

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

            Platform.runLater(() -> {
                progressBar.setProgress(1);
                outputTextArea.setText(resultContent.toString());
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
                outputTextArea.clear();
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
        // Inicializa com AES por padrão
        encryptionService = new EncryptionService(CryptographyType.AES);

        // Popula o ComboBox com todos os tipos de criptografia disponíveis
        algorithmComboBox.getItems().addAll(CryptographyType.values());
        algorithmComboBox.getSelectionModel().selectFirst();

        // Listener para mudança de algoritmo
        algorithmComboBox.setOnAction(event -> {
            CryptographyType selectedType = algorithmComboBox.getValue();
            encryptionService.setCryptographyType(selectedType);
        });

        // Opcional: Configurar como os itens devem ser exibidos
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

//    @FXML
//    public void handleEncryptFileUpload() {
//        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Supported Files",
//                ExtensionFile.TEXT.getExtension(), ExtensionFile.CSV.getExtension());
//        fileChooser.getExtensionFilters().add(extFilter);
//        selectedFile = fileChooser.showOpenDialog(new Stage());
//
//        if (selectedFile != null) {
//            inputTextArea.appendText(selectedFile.getAbsolutePath());
//        }
//    }
//
//    @FXML
//    public void handleEncryptFile() {
//        if (selectedFile != null) {
//            new Thread(() -> processFile(selectedFile, ENCRYPT)).start();
//        } else {
//            inputTextArea.appendText("No file selected for encryption.");
//        }
//    }
//
//    @FXML
//    public void handleDecryptFile() {
//        if (selectedFile != null) {
//            new Thread(() -> processFile(selectedFile, DECRYPT)).start();
//        } else {
//            outputTextArea.appendText("No file selected for decryption.");
//        }
//    }
//
//    private void processFile(File file, String action) {
//        try {
//            Platform.runLater(() -> progressBar.setProgress(0));
//            String content = new String(Files.readAllBytes(file.toPath()));
//            int totalLength = content.length();
//            int chunkSize = totalLength / 100; // Dividir o arquivo em 100 partes
//            StringBuilder resultContent = new StringBuilder();
//
//            for (int i = 0; i < 100; i++) {
//                int start = i * chunkSize;
//                int end = (i == 99) ? totalLength : (i + 1) * chunkSize;
//                String chunk = content.substring(start, end);
//
//                String processedChunk;
//                if (ENCRYPT.equals(action)) {
//                    processedChunk = encryptionService.encrypt(chunk);
//                } else {
//                    processedChunk = encryptionService.decrypt(chunk);
//                }
//
//                resultContent.append(processedChunk);
//
//                // Atualizar a barra de progresso
//                int progress = i + 1;
//                Platform.runLater(() -> progressBar.setProgress(progress / 100.0));
//
//                // Simular atraso para visualização da barra de progresso
//                try {
//                    Thread.sleep(20); // 20 milissegundos de atraso
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//
//            Platform.runLater(() -> {
//                progressBar.setProgress(1);
//                outputTextArea.appendText(resultContent.toString());
//                showAlert("Processo concluído com sucesso!", Alert.AlertType.INFORMATION);
//            });
//        } catch (IOException e) {
//            Platform.runLater(() -> {
//                progressBar.setProgress(0);
//                outputTextArea.appendText("Error during " + action + "ion: " + e.getMessage() + "\n");
//                showAlert("Erro durante o processo: " + e.getMessage(), Alert.AlertType.ERROR);
//            });
//        }
//    }
//
//    @FXML
//    public void handleCopyOutput() {
//        String output = outputTextArea.getText();
//        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
//        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
//        content.putString(output);
//        clipboard.setContent(content);
//
//        outputTextArea.appendText("Output copied to clipboard.");
//    }
//
//    @FXML
//    public void handleExportOutput() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Export Output");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
//        File file = fileChooser.showSaveDialog(new Stage());
//
//        if (file != null) {
//            try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
//                writer.write(outputTextArea.getText());
//                outputTextArea.clear();
//            } catch (Exception e) {
//                outputTextArea.appendText("Failed to export output: " + e.getMessage() + "\n");
//            }
//        }
//    }
//
//    @FXML
//    public void handleCleanAllInputs() {
//        inputTextArea.clear();
//        outputTextArea.clear();
//        algorithmComboBox.getSelectionModel().selectFirst();
//    }
//
//    private void showAlert(String message, Alert.AlertType alertType) {
//        Alert alert = new Alert(alertType, message, ButtonType.OK);
//        alert.setHeaderText(null);
//        alert.setTitle(alertType == Alert.AlertType.INFORMATION ? "Concluído" : "Erro");
//        alert.showAndWait();
//    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        algorithmComboBox.getItems().addAll(" ", "AES", "DES", "RSA");
//        algorithmComboBox.getSelectionModel().selectFirst();
//    }
}
