package br.com.fiap.market.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/itens").permitAll()
                        .requestMatchers(HttpMethod.GET, "/itens/novo").authenticated()
                        .requestMatchers(HttpMethod.POST, "/itens").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/itens/{id}/editar").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/itens/{id}/excluir").authenticated()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/itens", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
