package co.jht.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/", "/home", "/register", "/login",
                        "/css/**", "/js/**", "/images/**", "/static/favicon.ico"
                ).permitAll()
                .anyRequest()
                .authenticated()
            )
            .formLogin(form -> form
                .loginPage("/loginSuccess")
                .defaultSuccessUrl("/home", true) // Redirect to /home after successful login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // Redirect to /login after logout
                .permitAll()
            );
        return http.build();
    }
}