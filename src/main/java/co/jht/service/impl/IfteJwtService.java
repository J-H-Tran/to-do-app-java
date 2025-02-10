package co.jht.service.impl;

import co.jht.model.domain.entity.appuser.IfteUser;
import co.jht.repository.IfteTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class IfteJwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    private final IfteTokenRepository tokenRepository;

    public IfteJwtService(IfteTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractTokenUsername(String token) {
        String username = extractClaims(token, Claims::getSubject);
        return username;
    }

    // Checks token validity from UserDetails
    public boolean isValidAccessToken(String token, UserDetails user) {
        String username = extractTokenUsername(token);

        boolean validToken =
                    tokenRepository
                        .findByAccessToken(token)
                        .map(t -> !t.isLoggedOut())
                        .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }

    // Checks token validitiy from application User
    public boolean isValidRefreshToken(String token, IfteUser user) {
        String username = extractTokenUsername(token);

        boolean validRefreshToken =
                    tokenRepository
                        .findByRefreshToken(token)
                        .map(t -> !t.isLoggedOut())
                        .orElse(false);
        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validRefreshToken;
    }

    private boolean isTokenExpired(String token) {
        boolean isExpired = extractExpiration(token).before(new Date());
        return isExpired;
    }

    private Date extractExpiration(String token) {
        Date expirationDate = extractClaims(token, Claims::getExpiration);
        return expirationDate;
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    public String generateAccessToken(IfteUser user) {
        String accessToken = generateToken(user, accessTokenExpire);
        return accessToken;
    }

    public String generateRefreshToken(IfteUser user) {
        String refreshToken = generateToken(user, refreshTokenExpire);
        return refreshToken;
    }

    private String generateToken(IfteUser user, Long expireTime) {
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime * 1000))
                .signWith(getSignInKey())
                .compact();
        return token;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return secretKey;
    }
}