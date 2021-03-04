package com.epam.esm.filter;

import com.epam.esm.util.auth.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final static String AUTHORIZATION_KEY = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";

    public AuthenticationFilter() {
        super("/user/**");
        setAuthenticationSuccessHandler(((request, response, authentication)
                -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getRequestDispatcher(request.getServletPath() + request.getPathInfo())
                    .forward(request, response);
        }));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(AUTHORIZATION_KEY);
        if (token != null && token.startsWith(PREFIX_BEARER)) {
            token = token.substring(PREFIX_BEARER.length());
            TokenAuthentication authenticationToken = new TokenAuthentication(token);
            return getAuthenticationManager().authenticate(authenticationToken);
        }
        throw new AuthenticationServiceException("error due to authentication");
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
