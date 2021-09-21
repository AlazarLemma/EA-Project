package com.example.jwt.service;

import antlr.BaseAST;
import com.example.jwt.domain.User;
import com.example.jwt.domain.UserRegisteredEvent;
import com.example.jwt.domain.UserRole;
import com.example.jwt.integration.KafkaSender;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.UserRoleRepository;
import com.example.jwt.service.dto.UserDto;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private UserAdapterService adapterService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaSender sender;

    public User loadUserByUsername(String username) {
        Optional<User> user = repository.findByUsername(username);
        return user.orElse(null);
    }

    public UserDto registerUser(UserDto dto) {
        UUID uuid = Generators.timeBasedGenerator().generate();
        dto.setUuid(uuid.toString());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User user = adapterService.fromDto(dto);

        List<Long> roleIds = dto.getRoleIds();
        List<UserRole> roles = this.findManyRoles(roleIds);
        user.setRoles(roles);


        System.out.println(roles);

        System.out.println(user);

        try {
            repository.save(user);
        } catch(Exception ex) {}

        // publish user registered event to topic "user"
        UserRegisteredEvent event = adapterService.getUserRegisteredEvent(user);
        sender.sendUserRegistered("user-registered", event);

        return adapterService.fromUserToDto(user);
    }

    public List<UserRole> getRoles() {
        return (List<UserRole>) roleRepository.findAll();
    }

    public List<UserRole> findManyRoles(List<Long> roleIds) {
        return roleRepository.findMany(roleIds);
    }
}
