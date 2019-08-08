package com.auzmor.restapi.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auzmor.restapi.entity.Account;
import com.auzmor.restapi.repo.AccountRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//		.httpBasic()
//		.and()
//		.authorizeRequests()
//			.anyRequest().authenticated()
//			.and()
//		.formLogin().disable()
//		.csrf().disable();
		
		http
		.httpBasic()
		.and()
		.authorizeRequests()
			.antMatchers("/api/*").authenticated()
			.anyRequest().permitAll()
			.and()
		.formLogin().disable()
		.csrf().disable();		
		http.headers().frameOptions().disable();
		http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {

			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return rawPassword.toString().equals(encodedPassword);
			}        	
        };
    }
	
	@Service
	public static class UserDetailsServiceImpl implements UserDetailsService{

		@Autowired
		AccountRepository accountRepository;
		
		@Override
		@Transactional
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			
			Account account = accountRepository.findByUsername(username).orElseThrow(
			        () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));;
			
			return new UserDetails() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					return null;
				}

				@Override
				public String getPassword() {
					return account.getAuthId();
				}

				@Override
				public String getUsername() {
					return account.getUsername();
				}

				@Override
				public boolean isAccountNonExpired() {
					return true;
				}

				@Override
				public boolean isAccountNonLocked() {
					return true;
				}

				@Override
				public boolean isCredentialsNonExpired() {
					return true;
				}

				@Override
				public boolean isEnabled() {
					return true;
				}				
			};
		}		
	}
}
