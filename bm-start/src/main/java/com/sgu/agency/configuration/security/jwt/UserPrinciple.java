package com.sgu.agency.configuration.security.jwt;

import com.sgu.agency.common.enums.UserModelEnum;
import com.sgu.agency.dtos.response.security.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private UserModelEnum userModel;

    private Boolean isLimited;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(String username, String email, String password, UserModelEnum userModel, Boolean isLimited,
                         Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.userModel = userModel;
        this.isLimited = isLimited;
    }

    public static UserPrinciple build(UserDto user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> permissions = user.getPermissions();

        if (permissions != null) {
            for(String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + permission));
            }
        }

        return new UserPrinciple(
                user.getEmail(),
                user.getEmail(),
                user.getPassword(),
                user.getUserModel(),
                user.getIsLimited(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(username, user.username);
    }
}
