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

import rozaryonov.shipping.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired//todo learn autowiring differences
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean //todo learn bean scopes
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
		
		http.csrf().disable();

		http
			.authorizeRequests()
			.antMatchers(
				"/", 
				"/index", 
				"/cabinet", 
				"/costs", 
				"/delivery_cost", 
				"/login", 
				"/logout", 
				"/new",
				"/error/*").permitAll()
			.antMatchers("/manager/*").hasRole("MANAGER")
			.antMatchers("/auth_user/*").hasRole("USER")
			.and()
		.exceptionHandling()
			.accessDeniedPage("/error/403")
			.and()
		.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/j_spring_security_check")
			.defaultSuccessUrl("/dispatch")
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
