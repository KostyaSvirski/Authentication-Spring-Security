package com.epam.esm.util.jwt;

import com.epam.esm.auth.UserPrincipal;
import com.epam.esm.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final String SECRET = "secret";
    private static final int EXPIRATION_DAYS = 1;
    private static final String AUTHORITIES = "authorities";
    private static final String ID = "id";
    private static final String DELIMITER = ",";

    public String generateToken(UserDTO user) {
        Instant expiration = Instant.now().plus(Duration.ofDays(EXPIRATION_DAYS));
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER)));
        claims.put(ID, user.getId());
        return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).setExpiration(Date.from(expiration))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public boolean validateToken(String token) {
        Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        return true;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.get(AUTHORITIES, String.class);
    }

    public long getIdUserFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.get(ID, Long.class);
    }

    public UserPrincipal getPrincipal(String token) {
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(getLoginFromToken(token));
        principal.setId(getIdUserFromToken(token));
        return principal;
    }
}
