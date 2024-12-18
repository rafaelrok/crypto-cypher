package br.com.rafaelvieira.cryptocypher.domain.encrypt;

import br.com.rafaelvieira.cryptocypher.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
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
}
