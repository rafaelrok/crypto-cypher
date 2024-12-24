package br.com.rafaelvieira.cryptocypher.controller;

import br.com.rafaelvieira.cryptocypher.config.StageManager;
import br.com.rafaelvieira.cryptocypher.service.NavigationService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class SplashController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SplashController.class);

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private final NavigationService navigationService;

    @Autowired
    @Lazy
    private StageManager stageManager;

    private Timeline timeline;

    @Autowired
    public SplashController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @FXML
    public void initialize() {
        // Configura a animação da barra de progresso
        setupProgressAnimation();
    }

    private void setupProgressAnimation() {
        timeline = new Timeline();

        // Adiciona frames para a animação
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), 0),
                        new KeyValue(statusLabel.textProperty(), "Iniciando...")),

                new KeyFrame(Duration.seconds(1),
                        new KeyValue(progressBar.progressProperty(), 0.25),
                        new KeyValue(statusLabel.textProperty(), "Carregando configurações...")),

                new KeyFrame(Duration.seconds(2),
                        new KeyValue(progressBar.progressProperty(), 0.5),
                        new KeyValue(statusLabel.textProperty(), "Verificando serviços...")),

                new KeyFrame(Duration.seconds(3),
                        new KeyValue(progressBar.progressProperty(), 0.75),
                        new KeyValue(statusLabel.textProperty(), "Preparando interface...")),

                new KeyFrame(Duration.seconds(4),
                        new KeyValue(progressBar.progressProperty(), 1.0),
                        new KeyValue(statusLabel.textProperty(), "Concluído!"))
        );

        timeline.setOnFinished(e -> Platform.runLater(this::openLoginScreen));
        timeline.play();
    }

    private void openLoginScreen() {
        try {
            Thread.sleep(500);
            Platform.runLater(navigationService::navigateToLogin);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.error("Error opening login screen", e);
        }
    }

    // Método para cancelar a animação se necessário
    public void cancelTimeline() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
