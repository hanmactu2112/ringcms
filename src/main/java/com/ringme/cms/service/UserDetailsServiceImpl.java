package com.ringme.cms.service;

import com.ringme.cms.dto.UserSecurity;
import com.ringme.cms.model.User;
import com.ringme.cms.model.UserRole;
import com.ringme.cms.repository.UserRepository;
import com.ringme.cms.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    RoleRouterService roleRouterService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUserName(username);
        if (user.isPresent()){
            List<UserRole> userRoles = userRoleRepository.findUserRoleByUserId(user.get().getId());

            List<GrantedAuthority> grandList = userRoles.stream().map((e)->{
               return new SimpleGrantedAuthority(e.getRole().getRoleName());
            }).collect(Collectors.toList());

            List<Long> roleIds = userRoles.stream().map(e->e.getRole().getId()).collect(Collectors.toList());
            Set<String> routerLink = roleRouterService.findAllRouterRoleByListRoleId(roleIds).stream()
                    .map(e->e.getRouter().getRouter_link()).collect(Collectors.toSet());
//            UserSercurity userSercurity = UserSercurity.builder().;
            UserSecurity useSercurity =  new UserSecurity(user.get().getUserName(),user.get().getPassword(),grandList, user.get().getEmail(),user.get().getFullName(),user.get().getAddress(),user.get().getPhone(),user.get().getUserType(),user.get().getIdProvince());
            useSercurity.setRouter(routerLink);
            return useSercurity;
        }
        else {
            throw new UsernameNotFoundException("User " + username + " was not found in database");
        }
    }
}
