package co.jht.repository;

import co.jht.model.domain.entity.token.IfteToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IfteTokenRepository extends JpaRepository<IfteToken, Long> {

    @Query("""
        SELECT t
        FROM IfteToken t
        INNER JOIN IfteUser u
        ON t.user.id = u.id
        WHERE t.user.id = :id
        AND t.isLoggedOut = false
    """)
    List<IfteToken> findAllNotLoggedOutTokensByUserId(Long id);
    Optional<IfteToken> findByAccessToken(String token);
    Optional<IfteToken> findByRefreshToken(String token);
}