package co.jht.repository;

import co.jht.model.domain.persist.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByVerificationCode(String verificationCode);
    Optional<AppUser> findByUsername(String username);
}