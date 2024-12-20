package br.com.rafaelvieira.cryptocypher.payload.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.annotation.Nullable;;


@JsonTypeName("encryption")
public class EncryptionRequest {

    @Nullable
    private String type;

    @Nullable
    private String content;

    @Nullable
    private String fileType;

    @Nullable
    private String createdAt;

    private String updatedAt;

    @Nullable
    private String history;

    public EncryptionRequest(@Nullable String type,
                             @Nullable String content,
                             @Nullable String fileType,
                             @Nullable String createdAt,
                             String updatedAt,
                             @Nullable String history) {
        this.type = type;
        this.content = content;
        this.fileType = fileType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.history = history;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @Nullable
    public String getFileType() {
        return fileType;
    }

    public void setFileType(@Nullable String fileType) {
        this.fileType = fileType;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Nullable
    public String getHistory() {
        return history;
    }

    public void setHistory(@Nullable String history) {
        this.history = history;
    }
}
