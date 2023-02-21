package com.ringme.cms.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Collection;

@Getter
@Setter
@ToString
public class UserSercurity extends User {

    private String email;

    private String fullName;

    private String address;

    private String phone;

    private String userType;

    private Long idProvince;
    public UserSercurity(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserSercurity(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String fullName, String address, String phone, String userType, Long idProvince) {
        super(username, password, authorities);
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.userType = userType;
        this.idProvince = idProvince;
    }

    public UserSercurity(String username, String password, boolean enabled, boolean accountNonExpired,
                         boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
