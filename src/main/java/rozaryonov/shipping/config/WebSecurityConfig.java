package rozaryonov.shipping.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import rozaryonov.shipping.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// whithout Thymeleaf, there necessary to add <input type="hidden" name = "${_csrf.parameterName}" value = "${_csrf.token}">

		http
			.authorizeRequests()
			.antMatchers( //todo extract
				"/", 
				"/persons/*",
				"/cabinet", 
				"/costs", 
				"/delivery_cost", 
				"/login", 
				"/logout", 
				"/new",
				"/error/*",
                "/shippings/*").permitAll()
			.antMatchers("/manager/*").hasRole("MANAGER")
			.antMatchers("/auth_user/**").hasRole("USER")
			.and()
		.exceptionHandling()
			.accessDeniedPage("/error/403")
			.and()
		.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/j_spring_security_check")
			.defaultSuccessUrl("/authorized_zone_redirection")
			.failureUrl("/login?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
			.and()
		.logout()
			.logoutUrl("/logout")
			.clearAuthentication(true)
			.logoutSuccessUrl("/")
			.and()
		.rememberMe()
			.tokenRepository(this.persistentTokenRepository())
			.tokenValiditySeconds(1 * 24 * 60 * 60);


	}

	
	// Token stored in Memory (Of Web Server).
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
	    InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
	    return memory;
	}
	
}
