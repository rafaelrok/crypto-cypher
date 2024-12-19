package br.com.rafaelvieira.cryptocypher.view;

import java.util.ResourceBundle;

public enum ViewFxml {

    SPLASH_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("splash.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/splash-view.fxml";
        }
    },

    LOGIN_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("application.login.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/login-view.fxml";
        }
    },

    REGISTER_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("application.register.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/register-view.fxml";
        }
    },

    RESET_PASSWORD_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("application.reset.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/reset-password-view.fxml";
        }
    },

    MAIN_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("application.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/main-view.fxml";
        }
    },

    VERIFY_VIEW_FXML{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("application.verify.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/verify-view.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
