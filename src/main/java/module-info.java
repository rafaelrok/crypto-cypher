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
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.hibernate.orm.core;
    requires modelmapper;
    requires jakarta.annotation;
    requires spring.security.crypto;
    requires spring.security.web;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.tx;
    requires spring.data.jpa;
    requires spring.web;
    requires jakarta.servlet;
    requires jjwt.api;
    requires okhttp3;


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
    exports br.com.rafaelvieira.cryptocypher.config;
    exports br.com.rafaelvieira.cryptocypher.util;
    exports br.com.rafaelvieira.cryptocypher.enums;
    exports br.com.rafaelvieira.cryptocypher.domain.user;
    opens br.com.rafaelvieira.cryptocypher.domain.user to com.fasterxml.jackson.databind, spring.core;
    exports br.com.rafaelvieira.cryptocypher.domain.encrypt;
    opens br.com.rafaelvieira.cryptocypher.domain.encrypt to com.fasterxml.jackson.databind, spring.core;
    exports br.com.rafaelvieira.cryptocypher.domain.role;
    opens br.com.rafaelvieira.cryptocypher.domain.role to com.fasterxml.jackson.databind, spring.core;
    exports br.com.rafaelvieira.cryptocypher.payload;
    opens br.com.rafaelvieira.cryptocypher.payload to com.fasterxml.jackson.databind, spring.core;
}
