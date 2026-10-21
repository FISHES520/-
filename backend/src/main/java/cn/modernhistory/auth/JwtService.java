package cn.modernhistory.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final javax.crypto.SecretKey key; private final long expiration;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration}") long expiration) { this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expiration=expiration; }
    public String issue(UserAccount user) {
        Instant now=Instant.now();
        return Jwts.builder().subject(user.getEmail()).claim("roles", user.getRoles().stream().map(r -> "ROLE_"+r.getCode().name()).toList()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expiration))).signWith(key).compact();
    }
    public String subject(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject(); }
}
