package br.com.rafaelvieira.cryptocypher.view;

import java.util.ResourceBundle;

public enum ViewFxml {

    MAIN_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("application.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/main-view.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
