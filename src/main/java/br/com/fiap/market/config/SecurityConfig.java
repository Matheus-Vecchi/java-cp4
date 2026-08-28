package br.com.fiap.market.config;


import br.com.fiap.market.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http)
            throws Exception {
        http
                .csrf(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cadastro").permitAll()
                        .requestMatchers("/acesso-negado").permitAll()
                        // mvc que e o foco do cp1 parte 2
                        .requestMatchers(HttpMethod.GET, "/itens").permitAll()
                        .requestMatchers(HttpMethod.GET, "/itens/novo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/itens").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/itens/{id}/editar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/itens/{id}/editar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/itens/{id}/excluir").hasRole("ADMIN")
                        // agora as api antiga
                        .requestMatchers(HttpMethod.GET, "/mercado", "/mercado/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/mercado/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/mercado").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/mercado/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/mercado/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/mercado/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/itens", true)
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/acesso-negado")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder,
                                                 UsuarioRepository usuarioRepository) {
        UserDetails user = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        return username -> {
            if (username.equals(adminUsername)) {
                return user;
            } else {
                return usuarioRepository.findByUsername(username)
                        .map(usuario -> User.builder()
                                .username(usuario.getUsername())
                                .password(usuario.getSenha())
                                .roles(usuario.getRole())
                                .build())
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuário não encontrado: " + username
                                )
                        );
            }
        };
    }
}
