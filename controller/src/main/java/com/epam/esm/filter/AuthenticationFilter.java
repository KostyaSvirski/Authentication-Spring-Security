package com.epam.esm.filter;

import com.epam.esm.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final static String AUTHORIZATION_KEY = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";

    @Autowired
    private JwtProvider jwtProvider;

    public AuthenticationFilter() {
        super(new OrRequestMatcher(
                new AntPathRequestMatcher("/certificates/**", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/certificates/**", HttpMethod.DELETE.name()),
                new AntPathRequestMatcher("/certificates/**", HttpMethod.PATCH.name()),
                new AntPathRequestMatcher("/tags/**", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/tags/**", HttpMethod.DELETE.name()),
                new AntPathRequestMatcher("/users/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/orders/**", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/orders/**", HttpMethod.GET.name())));
        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader(AUTHORIZATION_KEY);
        if (token != null && token.startsWith(PREFIX_BEARER)) {
            token = token.substring(PREFIX_BEARER.length());
            if (jwtProvider.validateToken(token)) {
                List<GrantedAuthority> listOfAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(jwtProvider.getAuthoritiesFromToken(token));
                Authentication authentication = new UsernamePasswordAuthenticationToken(jwtProvider.getPrincipal(token), null,
                        listOfAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return authentication;
            }
        }
        throw new AuthenticationServiceException("error due to authentication");
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        super.doFilter(req, res, chain);
    }
}
