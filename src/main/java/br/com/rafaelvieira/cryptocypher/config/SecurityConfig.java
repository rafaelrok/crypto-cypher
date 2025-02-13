package br.com.rafaelvieira.cryptocypher.config;

import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.security.UserDetailsImpl;
import br.com.rafaelvieira.cryptocypher.security.jwt.AuthEntryPointJwt;
import br.com.rafaelvieira.cryptocypher.security.jwt.AuthTokenFilter;
import br.com.rafaelvieira.cryptocypher.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

                if (!user.isVerified()) {
                    throw new DisabledException("Por favor, verifique seu email primeiro");
                }

                if (!passwordEncoder.matches(password, user.getPassword())) {
                    throw new BadCredentialsException("Senha inválida");
                }

                List<GrantedAuthority> authorities = user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());

                return new UsernamePasswordAuthenticationToken(
                        new UserDetailsImpl(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getPassword(),
                                authorities
                        ),
                        password,
                        authorities
                );
            }
        };
    }
}


//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final UserDetailsServiceImpl userDetailsService;
//    private final AuthEntryPointJwt unauthorizedHandler;
//
//    public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
//        this.userDetailsService = userDetailsService;
//        this.unauthorizedHandler = unauthorizedHandler;
//    }
//
//    @Bean
//    public AuthTokenFilter authenticationJwtTokenFilter() {
//        return new AuthTokenFilter();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/auth/**").permitAll()
//                                .anyRequest().authenticated()
//                );
//
//        http.authenticationProvider(authenticationProvider());
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}
