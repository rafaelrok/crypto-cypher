package br.com.rafaelvieira.cryptocypher.model.encrypt;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import br.com.rafaelvieira.cryptocypher.enums.ExtensionFile;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "encryption")
public class Encryption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CryptographyType type;

    private String content;

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private ExtensionFile fileType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "encryption")
    private Set<EncryptionHistory> history;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CryptographyType getType() {
        return type;
    }

    public void setType(CryptographyType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ExtensionFile getFileType() {
        return fileType;
    }

    public void setFileType(ExtensionFile fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<EncryptionHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<EncryptionHistory> history) {
        this.history = history;
    }
}
