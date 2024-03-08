package com.spring.project.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/project/auth/**").permitAll()
                        .requestMatchers("/project/api/user/getUserProfileData").hasAnyRole("USER", "ADMIN", "TRAINER")
                        .requestMatchers("/project/api/user/updateUserProfile").hasAnyRole("USER", "ADMIN", "TRAINER")
                        .requestMatchers("/project/api/user/getTrainerClasses").hasAnyRole("TRAINER")
                        .requestMatchers("/project/api/admin/getTrainingClasses").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/project/api/admin/getTrainingClassById").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/project/api/user/classes").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/project/api/user/subscriptions").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/project/api/user/getReservationsByCourt").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("project/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("project/api/trainer/**").hasRole("TRAINER")
                        .requestMatchers("/project/api/user/**").hasRole("USER")


                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

   @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
