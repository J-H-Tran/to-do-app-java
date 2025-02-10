package co.jht.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfiguration(AuthenticationProvider authProvider, JwtAuthenticationFilter jwtAuthFilter) {
        this.authProvider = authProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
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
            )
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtTokenUtil jwtTokenUtil,
            UserDetailsService userDetailsService
    ) throws Exception {
        SecurityContextRepository secCtxtRepo = new HttpSessionSecurityContextRepository();

        http
            .securityContext(context -> context
                .securityContextRepository(secCtxtRepo)
            )

            .cors(Customizer.withDefaults())

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

            .addFilterBefore(
                new JwtRequestFilter(jwtTokenUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
            )

            .httpBasic(Customizer.withDefaults())

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
            );

        return http.build();
    }*/

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("https://localhost:8443", "http://localhost:8080")); //TODO: update backend url
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

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