package com.ringme.cms.config;


import com.ringme.cms.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
//    @Autowired
//    CaptchaFilter captchaFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login","/captcha.jpg").permitAll();
        http.addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class).authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login?time=expired")
                .maximumSessions(1)
                .expiredUrl("/login?time=expired")
                .and().and()
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login-error")
                        .defaultSuccessUrl("/index")
                        .usernameParameter("username")
                        .passwordParameter("password")

                ).authenticationProvider(authenticationProvider())
                .logout((logout) -> logout
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .csrf()
                .disable().headers().frameOptions().disable()
                .and().exceptionHandling().accessDeniedPage("/403");

        return http.build();
    }
    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**","/styles/**","/static/**","/captcha.jpg");
    }
    @Bean
    public FilterRegistrationBean<CustomFilter> customFilterRegistration() {
        FilterRegistrationBean<CustomFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(customFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public CustomFilter customFilter() {
        return new CustomFilter();
    }
    @Bean
    public CaptchaFilter captchaFilter() {
        return new CaptchaFilter();
    }
}
