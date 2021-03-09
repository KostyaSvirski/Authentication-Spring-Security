package com.epam.esm.config;

import com.epam.esm.filter.AuthenticationFilter;
import com.epam.esm.service.impl.AuthenticationUserService;
import com.epam.esm.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootConfiguration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ADMIN = "ADMIN";

    @Autowired
    private AuthenticationUserService service;
    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JwtProvider provider() {
        return new JwtProvider();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/signin", "/signup", "/certificates/**", "/tags/**").permitAll()
                .antMatchers("/users/{^[\\d]$}").authenticated()
                .antMatchers("/orders/{^[\\d]$}").authenticated()
                .antMatchers(HttpMethod.POST, "/certificates/**").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/tags/**").hasRole(ROLE_ADMIN)
                .antMatchers("/users/**").hasRole(ROLE_ADMIN)
                .antMatchers("/orders/**").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers(HttpMethod.DELETE).hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PATCH).hasRole(ROLE_ADMIN)
                .and()
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
