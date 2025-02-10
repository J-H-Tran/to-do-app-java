package co.jht.repository;

import co.jht.model.domain.entity.appuser.IfteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IfteUserRepository extends JpaRepository<IfteUser, Long> {
    Optional<IfteUser> findByUsername(String username);
}