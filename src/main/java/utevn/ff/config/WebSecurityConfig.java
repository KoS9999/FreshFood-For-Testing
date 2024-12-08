package utevn.ff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import utevn.ff.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
    private AuthenticationFailureHandler customFailureHandler;

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		
		// Admin page
		http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
		
		// Chưa đăng nhập sẽ trả về trang /login
		http.authorizeRequests().antMatchers("/checkout").access("hasRole('ROLE_USER')");
		
		http.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginProcessingUrl("/doLogin")
			.loginPage("/login")
			.defaultSuccessUrl("/?login_success")
			.successHandler(new SuccessHandler()).failureUrl("/login?error=true")
			.failureHandler(customFailureHandler) // Custom failure handler
			.permitAll()
			.and()
		.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/?logout_success")
			.permitAll();
			
		 // ghi nhớ đăng nhập
		http.rememberMe()
			.rememberMeParameter("remember");
		
	}

}
