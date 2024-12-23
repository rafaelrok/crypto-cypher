package br.com.rafaelvieira.cryptocypher;

import br.com.rafaelvieira.cryptocypher.config.StageManager;
import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AppCryptoCypher extends Application {

    private ConfigurableApplicationContext context;
    protected StageManager stageManager;

    @Override
    public void init() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CryptoCypherApplication.class);
        builder.headless(false);
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.context = builder.run(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UTILITY);
        stage.centerOnScreen();
        context.getBeanFactory().registerSingleton("primaryStage", stage);
        stageManager = context.getBean(StageManager.class, stage);
        displayInitialScene();
    }

    @Override
    public void stop() {
        context.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch();
    }

    protected void displayInitialScene() {
        stageManager.switchScene(ViewFxml.SPLASH_VIEW_FXML);
    }
}
