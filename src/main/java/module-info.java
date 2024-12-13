module br.com.rafaelvieira.cryptocypher {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires fontawesomefx;

    opens br.com.rafaelvieira.cryptocypher to javafx.fxml;
    exports br.com.rafaelvieira.cryptocypher;
    exports br.com.rafaelvieira.cryptocypher.controller;
    opens br.com.rafaelvieira.cryptocypher.controller to javafx.fxml;
    opens br.com.rafaelvieira.cryptocypher.domain to com.fasterxml.jackson.databind;
}
