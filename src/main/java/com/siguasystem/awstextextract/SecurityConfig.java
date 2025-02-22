package com.siguasystem.awstextextract;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

      @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}admin123") // {noop} indica que la contraseña no está cifrada
            .roles("ADMIN")
            .build();

        UserDetails user = User.builder()
            .username("user")
            .password("{noop}user123")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /*.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Habilita CSRF con cookie
            )*/
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Permite acceso a la API
				.requestMatchers("/api-tra/**").permitAll() // Permite acceso a la API
				.requestMatchers("/api-pdf/**").permitAll() // Permite acceso a la API
                .requestMatchers("/static/**").permitAll() // Permite recursos estáticos
                .anyRequest().permitAll()
            );

        return http.build();
    }

	/*@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		XorCsrfTokenRequestAttributeHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();
		// set the name of the attribute the CsrfToken will be populated on
		requestHandler.setCsrfRequestAttributeName(null);
		 http
			// ...
			.csrf((csrf) -> csrf
				.csrfTokenRequestHandler(requestHandler)
			);
		return http.build();
		http
		// ...
		.csrf(csrf ->csrf.disable())
		.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api-tra/**").permitAll() // Permite acceso público
                .anyRequest().permitAll()
		);
	return http.build();
	}*/
	/*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Almacena el token en una cookie
            )
            .authorizeRequests(auth -> auth
                .anyRequest().authenticated()
            );
        return http.build();
    }*/
}
