package com.epam.esm.jwt;

import com.epam.esm.dto.UserDTO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private static final String SECRET = "secret";
    private static final String EMAIL = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private BCryptPasswordEncoder encoder;

    public String generateToken(UserDTO user) {
        Instant expiration = Instant.now().plus(Duration.ofDays(15));
        Map<String, Object> claims = new HashMap<>();
        claims.put(EMAIL, user.getEmail());
        claims.put(PASSWORD, encoder.encode(user.getPassword()));
        return Jwts.builder().setClaims(claims).setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            return false;
        }
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.get(EMAIL, String.class);
    }
}
