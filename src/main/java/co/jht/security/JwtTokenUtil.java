//package co.jht.security.jwt;
//
//import co.jht.security.InMemoryTokenStore;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//import java.util.Date;
//
//@Component
//public class JwtTokenUtil {
//
//    private final InMemoryTokenStore inMemoryTokenStore;
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    @Autowired
//    public JwtTokenUtil(InMemoryTokenStore inMemoryTokenStore) {
//        this.inMemoryTokenStore = inMemoryTokenStore;
//    }
//
//    public String generateToken(String username) {
//        String token = Jwts.builder()
//                .subject(username)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .signWith(getSignInKey())
//                .compact();
//        inMemoryTokenStore.storeToken(username, token);
//        return token;
//    }
//
//    public boolean validateToken(String token, String username) {
//        String tokenUsername = getUsernameFromToken(token);
//        return (username.equals(tokenUsername) && !isTokenExpired(token));
//    }
//
//    public void invalidateToken(String username) {
//        inMemoryTokenStore.removeToken(username);
//    }
//
//    public String getUsernameFromToken(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractAllClaims(token).getExpiration().before(new Date());
//    }
//
//    private Claims extractAllClaims(String token){
//        return Jwts.parser()
//                .verifyWith(getSignInKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    private SecretKey getSignInKey() {
//        byte[] bytes = Base64.getDecoder().decode(secret);
//        return new SecretKeySpec(bytes, "HmacSHA256");
//    }
//}