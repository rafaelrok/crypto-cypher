package br.com.rafaelvieira.cryptocypher.payload.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonTypeName("encryption")
public class EncryptionResponse {

    private Long id;
    private String type;
    private String content;
    private String fileType;
    private String createdAt;
    private String updatedAt;
    private String history;
}
