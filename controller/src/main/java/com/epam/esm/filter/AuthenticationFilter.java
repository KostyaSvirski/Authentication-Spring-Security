package com.epam.esm.filter;

import com.epam.esm.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:/controller.properties")
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTHORIZATION_KEY = "uthorization";
    private static final String PREFIX_BEARER = "Bearer ";
    private static final String PATTERN_CERTIFICATES = "/certificates/**";
    private static final String PATTERN_TAGS = "/tags/**";
    private static final String PATTERN_USERS = "/users/**";
    private static final String PATTERN_ORDERS = "/orders/**";

    @Autowired
    private JwtProvider jwtProvider;

    public AuthenticationFilter() {
        super(new OrRequestMatcher(
                new AntPathRequestMatcher(PATTERN_CERTIFICATES, HttpMethod.POST.name()),
                new AntPathRequestMatcher(PATTERN_CERTIFICATES, HttpMethod.DELETE.name()),
                new AntPathRequestMatcher(PATTERN_CERTIFICATES, HttpMethod.PATCH.name()),
                new AntPathRequestMatcher(PATTERN_TAGS, HttpMethod.POST.name()),
                new AntPathRequestMatcher(PATTERN_TAGS, HttpMethod.DELETE.name()),
                new AntPathRequestMatcher(PATTERN_USERS, HttpMethod.GET.name()),
                new AntPathRequestMatcher(PATTERN_ORDERS, HttpMethod.POST.name()),
                new AntPathRequestMatcher(PATTERN_ORDERS, HttpMethod.GET.name())));

        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
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
