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
        // Desabilita a inicialização automática do StageManager
        builder.headless(false);
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.context = builder.run(args);
    }

//    @Override
//    public void init() {
//        String[] args = getParameters().getRaw().toArray(new String[0]);
//        this.context = new SpringApplicationBuilder()
//                .sources(CryptoCypherApplication.class)
//                .run(args);
//    }

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(AppCryptoCypher.class.getResource("view/main-view.fxml"));
//        fxmlLoader.setControllerFactory(context::getBean);
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("crypto-cypher");
//        stage.setScene(scene);
//        stage.show();
//    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
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
