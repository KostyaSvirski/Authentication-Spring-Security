package com.epam.esm.util.auth.manager;

import com.epam.esm.util.auth.TokenAuthentication;
import com.epam.esm.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationManager implements AuthenticationManager {

    @Autowired
    private JwtProvider provider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof TokenAuthentication) {
            TokenAuthentication customAuthentication = (TokenAuthentication) authentication;
            String token = customAuthentication.getToken();
            if (provider.validateToken(token)) {
                return new TokenAuthentication(token, provider.getAuthoritiesFromToken(token),
                        provider.getLoginFromToken(token), true);
            }
        }
        return new TokenAuthentication();
    }
}
