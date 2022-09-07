package com.ecom.mobile.accessories.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	private static List<String> clients = Arrays.asList("google");
//	
//	@Override
//	   protected void configure(HttpSecurity http) throws Exception {
//	      http
//	         .csrf()
//	         .disable()
//	         .antMatcher("/**")
//	         .authorizeRequests()
//	         .antMatchers("/", 	"/oauth2/authorization/google", "/index.html")
//	         .permitAll()
//	         .anyRequest()
//	         .authenticated();
//	   }
//	
//	@Bean
//	public ClientRegistrationRepository clientRegistrationRepository() {
//		List<ClientRegistration> registrations = clients.stream().map(c->getRegistration(c)).filter(registration->registration != null).collect(Collectors.toList());
//		
//		return new InMemoryClientRegistrationRepository(registrations);
//		
//	}
//	private ClientRegistration getRegistration(String client) {
//		String clientId = "892978107843-2tjo66rfuta4jjupq7epkcn2crnnfngu.apps.googleusercontent.com";
//		if(client.equals("google")) {
//			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret("s99ktLwTKduJmtI1DupyWg2R").build();
//		}
//		return null;
//	}
//	
//@Bean
//public OAuth2AuthorizedClientService authorizedClientService() {
//	return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
//}

	@Autowired
	CustomUserAuthenticationProvider customUserAuthenticationProvider;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	MyLogoutSuccessHandler myLogoutSuccessHandler;

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/js/**", "/css/**", "/fonts/**", "/images/**", "/img/**", "/capcha/**", "/register",
						"/validate", "/Home", "/index/**")
				.permitAll().anyRequest().authenticated().and().formLogin().failureUrl("/login?error")
				.loginPage("/login").defaultSuccessUrl("/Home").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/Home").permitAll().and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/Home").logoutSuccessHandler(myLogoutSuccessHandler).permitAll().and()
				.exceptionHandling().accessDeniedPage("/403").and().sessionManagement().sessionFixation()
				.migrateSession().invalidSessionUrl("/Home").maximumSessions(1).expiredUrl("/login?error")
				.maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry());

		http.headers().httpStrictTransportSecurity();
		http.headers().frameOptions();
		http.headers().xssProtection();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(customUserAuthenticationProvider);
	}

}