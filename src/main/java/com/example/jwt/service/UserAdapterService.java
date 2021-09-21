package com.example.jwt.service;

import com.example.jwt.domain.User;
import com.example.jwt.domain.UserRole;
import com.example.jwt.service.dto.AuthUserDetails;
import com.example.jwt.service.dto.AuthUserSubject;
import com.example.jwt.service.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdapterService {
    public UserDetails fromUser(User user) {
        Collection<UserRole> roles = user.getRoles();
        List<GrantedAuthority> grantedAuthorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        return new AuthUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                grantedAuthorities
        );
    }

    public AuthUserSubject fromUserToAuthSubject(User user) {
        List<String> roles = user.getRoles().stream().map(role -> role.getRoleName()).collect(Collectors.toList());
        return new AuthUserSubject(user.getUsername(), roles, user.getUuid(), user.isActive());
    }

    public User fromDto(UserDto dto) {
        return new User(dto.getUsername(), dto.getPassword(), true, dto.getUuid());
    }

    public UserDto fromUserToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getUuid(), user.isActive(), null);
    }
}
