package br.com.rafaelvieira.cryptocypher.repository;

import java.util.Optional;

import br.com.rafaelvieira.cryptocypher.model.role.Role;
import br.com.rafaelvieira.cryptocypher.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
