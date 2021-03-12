package com.epam.esm.filter;

import com.epam.esm.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final static String AUTHORIZATION_KEY = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";
    private static final String patternCertificates = "/certificates/**";
    private static final String patternTags = "/tags/**";
    private static final String patternUsers = "/users/**";
    private static final String patternOrders = "/orders/**";

    @Autowired
    private JwtProvider jwtProvider;

    public AuthenticationFilter() {
        super(new OrRequestMatcher(
                new AntPathRequestMatcher(patternCertificates, HttpMethod.POST.name()),
                new AntPathRequestMatcher(patternCertificates, HttpMethod.DELETE.name()),
                new AntPathRequestMatcher(patternCertificates, HttpMethod.PATCH.name()),
                new AntPathRequestMatcher(patternTags, HttpMethod.POST.name()),
                new AntPathRequestMatcher(patternTags, HttpMethod.DELETE.name()),
                new AntPathRequestMatcher(patternUsers, HttpMethod.GET.name()),
                new AntPathRequestMatcher(patternOrders, HttpMethod.POST.name()),
                new AntPathRequestMatcher(patternOrders, HttpMethod.GET.name())));

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
