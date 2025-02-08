package co.jht.config;

import co.jht.security.filter.JwtRequestFilter;
import co.jht.security.jwt.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtTokenUtil jwtTokenUtil,
            UserDetailsService userDetailsService
    ) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .requiresChannel(ch -> ch
                .requestMatchers("/auth/authenticate/**")
                    .requiresSecure()

                .anyRequest()
                    .requiresInsecure()
            )
            .securityMatchers(matchers -> matchers
                .requestMatchers("/users/**", "/tasks/**")
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health")
                    .permitAll()

                .requestMatchers("/users/update/**", "/users/delete/**", "/users/profile/**")
                    .hasAnyRole("ADMIN", "USER")

                .requestMatchers("/tasks/**")
                    .hasAnyRole("ADMIN", "USER")

                .requestMatchers("/users/**")
                    .hasRole("ADMIN")

                .anyRequest()
                    .authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionFixation().none()
            )
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(
                new JwtRequestFilter(jwtTokenUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}