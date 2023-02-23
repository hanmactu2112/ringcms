package com.ringme.cms.config;

import com.ringme.cms.Service.RoleRouterService;
import com.ringme.cms.Service.RoleService;
import com.ringme.cms.Service.UserDetailsServiceImpl;
import com.ringme.cms.model.Role;
import com.ringme.cms.model.RouterRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SercurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

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
//        List<Role> routerRoles = roleService.findAllRole();
        http.authorizeRequests().antMatchers("/login").permitAll();
//        for (Role role :routerRoles){
//            List<RouterRole> routerRoles1 = roleRouterService.findAllRouterRoleByRoleId(role.getId());
//            String[] arrayRouter = routerRoles1.stream().map((e) -> {
//                return e.getRouter().getRouter_link();
//            }).toArray(String[]::new);
            http
                    .authorizeRequests()
                    .antMatchers("/**").authenticated();
//        }
        http.authorizeRequests().and()
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll().failureUrl("/login")
                        .defaultSuccessUrl("/index").usernameParameter("username")
                        .passwordParameter("password")
                ).authenticationProvider(authenticationProvider())
                .logout((logout) -> logout.permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .csrf()
                .disable().headers().frameOptions().disable().and().exceptionHandling().accessDeniedPage("/403");
        return http.build();
    }
    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public CustomFilter customFilter() {
        return new CustomFilter();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**","/styles/**","/static/**");
    }
    @Bean
    public FilterRegistrationBean<CustomFilter> customFilterRegistration() {
        FilterRegistrationBean<CustomFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(customFilter());
        registration.addUrlPatterns("/*"); // kiểm tra tất cả các request
        return registration;
    }


}
