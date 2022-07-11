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
  private final String homeAssistantEntitiesEndpoint = "/entities/hass";
  private final String homsaiEntitiesEndpoint = "/entities/homsai";
  private final String excludedEntitiesEndpoint = "/entities/hass/excluded";
  private final String homsaiEntitiesValuesEndpoint = "/entities/history/homsai";
  private final String injectTokenEndpoint = "/auth/token/inject";
  private final String removeTokenEndpoint = "/auth/token/remove";
  private final String isLoggedEndpoint = "/auth/islogged";
  private final String settingsEndpoint = "/settings";
  private final String hvacInitEndpoint = "/entities/homsai/hvac/init";
  private final String hvacInitStatusEndpoint = "/entities/homsai/hvac/init/status";
  private final String hvacListEndpoint = "/entities/homsai/hvac";
  private final String hvacDetailEndpoint = "/entities/homsai/hvac/*";
  private final String hvacDeviceSetSettingsEndpoint = "/entities/homsai/hvac/settings/*";

  @Autowired
  AIServiceAuthenticationProvider aiServiceAuthenticationProvider;

  /**
   * Require login to access internal pages and configure login form.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable()

        .requestCache().requestCache(new CustomRequestCache())

        .and().authorizeRequests()

        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
            .antMatchers( "/").permitAll()
            .antMatchers(homeAssistantEntitiesEndpoint).permitAll()
            .antMatchers(homsaiEntitiesEndpoint).permitAll()
            .antMatchers(HttpMethod.POST, excludedEntitiesEndpoint).permitAll()
            .antMatchers(HttpMethod.GET, homsaiEntitiesValuesEndpoint).permitAll()
            .antMatchers(HttpMethod.GET, LOGIN_URL).permitAll()
            .antMatchers(HttpMethod.POST, injectTokenEndpoint).permitAll()
            .antMatchers(HttpMethod.POST, removeTokenEndpoint).permitAll()
            .antMatchers(HttpMethod.GET, isLoggedEndpoint).permitAll()
            .antMatchers(HttpMethod.POST, hvacInitEndpoint).permitAll()
            .antMatchers(HttpMethod.GET, hvacInitStatusEndpoint).permitAll()
            .antMatchers(HttpMethod.GET, hvacListEndpoint).permitAll()
            .antMatchers(HttpMethod.GET, hvacDetailEndpoint).permitAll()
            .antMatchers(HttpMethod.POST, hvacDeviceSetSettingsEndpoint).permitAll()
            .antMatchers(settingsEndpoint).permitAll()
        .anyRequest().authenticated()
        .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

    http.headers().frameOptions().disable();
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