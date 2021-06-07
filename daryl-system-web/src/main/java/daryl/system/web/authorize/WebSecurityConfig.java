package daryl.system.web.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
@EnableWebSecurity
@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
	
    @Autowired
    UserDetailServiceImpl userDetailsService;
	
    //Registra el service para usuarios y el encriptador de contrasena
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
 
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());     
    }
	
	@Bean 
	public PasswordEncoder passwordEncoder() { 
	    return new Pbkdf2PasswordEncoder("darylsystemproject");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/assets/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		 http 
	      .csrf().disable()
	      .authorizeRequests()	
	      	.antMatchers("/robots/cuenta").hasRole("USER")
	      	.antMatchers(HttpMethod.POST, "/robots/cuenta/**").hasRole("USER")
	      	.antMatchers("/login").permitAll() 
	      	.antMatchers("/").permitAll()
	      	.antMatchers("/dashboard").permitAll()
	      	.antMatchers("/robots").permitAll()
	      	.antMatchers("/robots/red").permitAll()
	      	.antMatchers("/robots/green").permitAll()
	      	.antMatchers("/robots/buscar").permitAll()
	      	.antMatchers("/robot/*").permitAll()
	      	.antMatchers("/chart/**").permitAll()
	      	.antMatchers("/demolab/**").permitAll()
	      	.antMatchers("/backtest/**").permitAll()
	      .anyRequest().authenticated()
	      .and() 
	      .formLogin().loginPage("/login").loginProcessingUrl("/login").successForwardUrl("/robots/cuenta")
	      .usernameParameter("username")
	      .passwordParameter("password")
	      .and() 
	      .logout().logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true).permitAll().logoutSuccessUrl("/login"); 

		
	}
	

	
}
