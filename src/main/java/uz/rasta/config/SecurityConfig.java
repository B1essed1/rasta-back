package uz.rasta.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints - public
                        .requestMatchers("/api/auth/**").permitAll()

                        // Public marketplace / storefront endpoints
                        .requestMatchers(HttpMethod.GET, "/api/shops").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shops/{handle}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shops/{shopId}/config").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shops/{shopId}/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shops/{shopId}/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shops/{shopId}/reviews").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/shops/{shopId}/orders").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/shops/{shopId}/reviews").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/shops/{shopId}/orders/{id}/cancel").permitAll()

                        // Categories - public read
                        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // Categories - admin only write
                        .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // Media upload requires auth, no public GET needed (MinIO serves files directly)
                        .requestMatchers(HttpMethod.POST, "/api/media").authenticated()

                        // Marketplace discovery
                        .requestMatchers(HttpMethod.GET, "/api/marketplace/**").permitAll()

                        // Everything else requires auth
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
