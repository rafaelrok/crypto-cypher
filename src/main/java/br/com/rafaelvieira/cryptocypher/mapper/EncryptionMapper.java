package br.com.rafaelvieira.cryptocypher.mapper;

import br.com.rafaelvieira.cryptocypher.model.encrypt.Encryption;
import br.com.rafaelvieira.cryptocypher.payload.request.EncryptionRequest;
import br.com.rafaelvieira.cryptocypher.payload.response.EncryptionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EncryptionMapper {

    private final ModelMapper modelMapper;

    public EncryptionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EncryptionResponse toResponse(Encryption encryption) {
        return modelMapper.map(encryption, EncryptionResponse.class);

//        return modelMapper.map(encryption, EncryptionResponse.class);
//        return EncryptionResponse.builder()
//                .id(encryption.getId())
//                .type(String.valueOf(encryption.getType()))
//                .content(encryption.getContent())
//                .fileType(String.valueOf(encryption.getFileType()))
//                .createdAt(encryption.getCreatedAt().toString())
//                .updatedAt(encryption.getUpdatedAt().toString())
//                .history(encryption.getHistory().toString())
//                .build();
    }

    public Encryption toEntity(EncryptionRequest request) {
        return modelMapper.map(request, Encryption.class);
    }

    public void copyToEntity(EncryptionRequest request, Encryption encryption) {
        modelMapper.map(request, encryption);
    }
}
