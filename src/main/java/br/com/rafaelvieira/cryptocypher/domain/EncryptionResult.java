package br.com.rafaelvieira.cryptocypher.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncryptionResult {

    @JsonProperty("content")
    private String content;

    @JsonProperty("filePath")
    private final String filePath;

    public EncryptionResult(String content, String filePath) {
        this.content = content;
        this.filePath = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {

    }
}
