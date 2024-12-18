package br.com.rafaelvieira.cryptocypher.domain.encrypt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EncryptionResult {

    @JsonProperty("content")
    private String content;

    @JsonProperty("filePath")
    private final String filePath;

    public EncryptionResult(String content, String filePath) {
        this.content = content;
        this.filePath = filePath;
    }
}
