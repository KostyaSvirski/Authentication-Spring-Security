package com.epam.esm.util.jwt;

import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserDTO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private static final String SECRET = "secret";
    private static final String EMAIL = "username";
    private static final String AUTHORITIES = "authorities";

    public String generateToken(UserDTO user) {
        Instant expiration = Instant.now().plus(Duration.ofDays(15));
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES, user.getAuthorities());
        return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public boolean validateToken(String token) {
        Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        return true;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.get(EMAIL, String.class);
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return (Collection<? extends GrantedAuthority>) claims.get(AUTHORITIES);
    }
}
