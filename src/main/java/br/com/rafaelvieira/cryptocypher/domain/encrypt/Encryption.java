package br.com.rafaelvieira.cryptocypher.domain.encrypt;

import br.com.rafaelvieira.cryptocypher.enums.CryptographyType;
import br.com.rafaelvieira.cryptocypher.enums.ExtensionFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
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
}
