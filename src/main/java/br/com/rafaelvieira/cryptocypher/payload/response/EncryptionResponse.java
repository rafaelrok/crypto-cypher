package br.com.rafaelvieira.cryptocypher.payload.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("encryption")
public class EncryptionResponse {

    private Long id;
    private String type;
    private String content;
    private String fileType;
    private String createdAt;
    private String updatedAt;
    private String history;

    public EncryptionResponse(Long id,
                              String type,
                              String content,
                              String fileType,
                              String createdAt,
                              String updatedAt,
                              String history) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.fileType = fileType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
