package it.uniroma3.siw.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.siw.model.Credentials;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

	@Autowired
	private DataSource dataSource;

	@Bean
	public UserDetailsManager userDetailsService() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		manager.setUsersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
		manager.setAuthoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?");
		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(CsrfConfigurer::disable).cors(CorsConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.GET, "/", "/index", "/libri", "/autori", "/autori/*","recensioni/*" ,"/register",
								"/libri/*", "/css/**", "/images/**", "js/**", "/fragments/**", "/favicon.ico",
								"/image/**")
						.permitAll().requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
						.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
						.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.loginPage("/login").permitAll()
						 .defaultSuccessUrl("/", true)
						.failureUrl("/login?error=true"))
				// LOGOUT: qui definiamo il logout
				.logout(logout -> logout
						// il logout è attivato con una richiesta GET a "/logout"
						.logoutUrl("/logout")
						// in caso di successo, si viene reindirizzati alla home
						.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true)
						.permitAll())
				.build();

	}
}
