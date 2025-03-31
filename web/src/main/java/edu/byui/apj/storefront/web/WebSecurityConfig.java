package edu.byui.apj.storefront.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                // TODO: update this to authorize any request, except for requests to /user-profile.html
                .requestMatchers("/user-profile.html").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/perform-login")
                .failureUrl("/login.html?error=Invalid+Login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/perform-logout") // Change logout URL
                .logoutSuccessUrl("/index.html") // Redirect after logout
                .invalidateHttpSession(true) // Invalidate session
                .clearAuthentication(true) // Remove authentication info
                .deleteCookies("JSESSIONID"))
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * For your information, to remove the deprecated static <code>User</code> method
     * <code><em>withDefaultPasswordEncoder()</em></code>, follow below instructions.
     * <pre>
     * {@code
     * // 1. Add PasswordEncoder as a parameter of the UserDetailsService Bean:
     * public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {...}
     *
     * // 2. Instead of
     * User.withDefaultPasswordEncoder()
     * // use
     * User.builder()
     *     .passwordEncoder(passwordEncoder::encode)
     *
     * // 3. Create Bean for PasswordEncoder as follows:
     * @Bean
     * public PasswordEncoder passwordEncoder() {
     *     return PasswordEncoderFactories.createDelegatingPasswordEncoder();
     * }
     * } <!-- @code -->
     * </pre>
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withDefaultPasswordEncoder()
                .username("apj-user")
                .password("password123")
                .roles("USER")
                .build()
        );
    }

}
