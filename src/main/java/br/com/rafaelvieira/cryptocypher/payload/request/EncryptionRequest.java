package br.com.rafaelvieira.cryptocypher.payload.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
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
}
