package app.homsai.engine.common.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/login";
  private static final String LOGIN_FAILURE_URL = "/login?error";
  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = "/login";

  @Autowired
  AIServiceAuthenticationProvider aiServiceAuthenticationProvider;

  /**
   * Require login to access internal pages and configure login form.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Vaadin handles CSRF internally
    http.csrf().disable()

        // Register our CustomRequestCache, which saves unauthorized access attempts, so the user is redirected after login.
        .requestCache().requestCache(new CustomRequestCache())

        // Restrict access to our application.
        .and().authorizeRequests()

        // Allow all Vaadin internal requests.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
            .antMatchers( "/").permitAll()
            .antMatchers("/entities/hass").permitAll()
            .antMatchers("/entities/homsai").permitAll()
            .antMatchers(HttpMethod.POST, "/entities/hass/excluded").permitAll()
            .antMatchers(HttpMethod.POST, "/entities/homsai/hvac/init").permitAll()
            .antMatchers(HttpMethod.GET, "/entities/history/homsai").permitAll()
            .antMatchers(HttpMethod.GET, "/login").permitAll()

        // Allow all requests by logged-in users.
        .anyRequest().authenticated()

        // Configure the login page.
     /*   .and().formLogin()
        .loginPage(LOGIN_URL).permitAll()
        .loginProcessingUrl(LOGIN_PROCESSING_URL)
        .failureUrl(LOGIN_FAILURE_URL)*/

        // Configure logout
        .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

    http.headers().frameOptions().disable(); //TODO REMOVE
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(aiServiceAuthenticationProvider);
  }


  /**
   * Allows access to static resources, bypassing Spring Security.
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        // Client-side JS
        "/VAADIN/**",

        // the standard favicon URI
        "/favicon.ico",

        // the robots exclusion standard
        "/robots.txt",

        // web application manifest
        "/manifest.webmanifest",
        "/sw.js",
        "/offline.html",

        // icons and images
        "/icons/**",
        "/images/**",
        "/styles/**",

        // (development mode) H2 debugging console
        "/h2-console/**");
  }
}