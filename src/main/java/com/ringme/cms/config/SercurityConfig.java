package com.ringme.cms.config;

import com.ringme.cms.Service.RoleRouterService;
import com.ringme.cms.Service.RoleService;
import com.ringme.cms.Service.UserDetailsServiceImpl;
import com.ringme.cms.model.Role;
import com.ringme.cms.model.RouterRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SercurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    RoleRouterService roleRouterService;

    @Autowired
    RoleService roleService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        List<Role> routerRoles = roleService.findAllRole();
        for (Role role :routerRoles){
            List<RouterRole> routerRoles1 = roleRouterService.findAllRouterRoleByRoleId(role.getId());
            String[] arrayRouter = routerRoles1.stream().map((e) -> {
                return e.getRouter().getRouter_link();
            }).toArray(String[]::new);
            http
                    .authorizeHttpRequests()
                    .antMatchers(arrayRouter).hasRole(role.getRoleName());
        }
        http
                .formLogin((form) -> form
                        .loginPage("/admin/login")
                        .permitAll().failureUrl("/admin/login-error")
                        .defaultSuccessUrl("/default",true).usernameParameter("email")
                        .passwordParameter("password")
                ).authenticationProvider(authenticationProvider())
                .logout((logout) -> logout.permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                        .logoutSuccessUrl("/admin/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .csrf()
                .disable().headers().frameOptions().disable();
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**","/slider","/slider/**","/templates","/templates/**","/resources/**","/styles/**","/static/**","/resources/adminImages/**","/adminImages/**");
    }
}
