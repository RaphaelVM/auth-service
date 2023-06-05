package com.drossdrop.authservice.config;

import com.drossdrop.authservice.entity.UserCredential;
import com.drossdrop.authservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private String roleName;

    @Autowired
    UserCredentialRepository userCredentialRepository;

    public CustomUserDetails(UserCredential userCredential) {
        this.username = userCredential.getUsername();
        this.password = userCredential.getPassword();
        this.roleName = userCredentialRepository.findByUsername(userCredential.getUsername()).get().getRoleName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleName));

        System.out.println("authorities list: " + authorities);

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRoleName() {
        return roleName;
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
}
