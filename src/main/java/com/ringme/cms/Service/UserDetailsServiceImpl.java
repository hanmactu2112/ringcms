package com.ringme.cms.Service;

import com.ringme.cms.dto.UserSercurity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUserName(username);
        if (user.isPresent()){
            List<UserRole> userRoles = userRoleRepository.findUserRoleByUserId(user.get().getId());
            List<GrantedAuthority> grandList = userRoles.stream().map((e)->{
               return new SimpleGrantedAuthority(e.getRole().getRoleName());
            }).collect(Collectors.toList());
            UserSercurity userSercurity =  new UserSercurity(user.get().getUserName(),user.get().getPassword(),grandList, user.get().getEmail(),user.get().getFullName(),user.get().getAddress(),user.get().getPhone(),user.get().getUserType(),user.get().getIdProvince());
            return userSercurity;
        }
        else {
            throw new UsernameNotFoundException("User " + username + " was not found in database");
        }
    }
}
