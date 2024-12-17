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
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.boot;
    requires static lombok;
    requires spring.beans;
    requires spring.core;
    requires org.slf4j;

    opens br.com.rafaelvieira.cryptocypher to javafx.fxml, spring.core, spring.beans, spring.context;
    opens br.com.rafaelvieira.cryptocypher.controller to javafx.fxml, spring.core, spring.beans, spring.context;
    opens br.com.rafaelvieira.cryptocypher.domain to com.fasterxml.jackson.databind, spring.core;
    opens br.com.rafaelvieira.cryptocypher.config to spring.core, spring.beans, spring.context;
    opens br.com.rafaelvieira.cryptocypher.service to spring.core, spring.beans, spring.context;
    opens br.com.rafaelvieira.cryptocypher.service.impl to spring.core, spring.beans, spring.context;
    opens br.com.rafaelvieira.cryptocypher.util to spring.core;

    exports br.com.rafaelvieira.cryptocypher;
    exports br.com.rafaelvieira.cryptocypher.controller;
    exports br.com.rafaelvieira.cryptocypher.service;
    exports br.com.rafaelvieira.cryptocypher.service.impl;
    exports br.com.rafaelvieira.cryptocypher.domain;
    exports br.com.rafaelvieira.cryptocypher.config;
    exports br.com.rafaelvieira.cryptocypher.util;
    exports br.com.rafaelvieira.cryptocypher.enums;
}
