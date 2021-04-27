package daryl.system.web.authorize;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("user").password("{noop}password").roles("USER");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		http 
			.csrf() 
			.disable()
			//.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			//.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				//.antMatchers("/assets/**").permitAll()
				//.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers("/**").hasAnyRole("USER")
				.anyRequest().authenticated()
				.and()
			.formLogin().loginPage("/");
		*/

		/*
		http.csrf().disable()
			//.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers("/assets/**").permitAll()
			.antMatchers(HttpMethod.GET, "/**").hasAnyRole("USER")
			.antMatchers("/").permitAll()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.and()
			.formLogin().loginPage("/").permitAll().successForwardUrl("/dashboard")
            .and()
            .logout()
            .permitAll()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/");
			*(
		
		/*
		http.csrf().disable()
			.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/").permitAll() //permitimos el acceso a / a cualquiera
			.anyRequest().authenticated() //cualquier otra peticion requiere autenticacion
			.and()
            .formLogin().loginPage("/").permitAll().successForwardUrl("/dashboard")
            .and()
            .logout()
            .permitAll()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/");
        */
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/assets/**");
	}
	
}
