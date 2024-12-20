package br.com.rafaelvieira.cryptocypher.model.encrypt;

import br.com.rafaelvieira.cryptocypher.model.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "encryption_history")
public class EncryptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "encryption_id")
    private Encryption encryption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Encryption getEncryption() {
        return encryption;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }
}
