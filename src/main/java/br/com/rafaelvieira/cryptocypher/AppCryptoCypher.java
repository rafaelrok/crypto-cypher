package br.com.rafaelvieira.cryptocypher;

import br.com.rafaelvieira.cryptocypher.config.StageManager;
import br.com.rafaelvieira.cryptocypher.view.ViewFxml;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AppCryptoCypher extends Application {

    private ConfigurableApplicationContext context;
    protected StageManager stageManager;
    private static final Logger log = LoggerFactory.getLogger(AppCryptoCypher.class);

    @Override
    public void init() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CryptoCypherApplication.class);
        builder.headless(false)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
//        String[] args = getParameters().getRaw().toArray(new String[0]);
//        this.context = builder.run(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        context.getBeanFactory().registerSingleton("primaryStage", stage);
        stageManager = context.getBean(StageManager.class, stage);

        stage.initStyle(StageStyle.DECORATED);
        stage.centerOnScreen();

        stage.show();
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
