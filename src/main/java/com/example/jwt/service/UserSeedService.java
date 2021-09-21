package com.example.jwt.service;

import com.example.jwt.domain.User;
import com.example.jwt.domain.UserRole;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.UserRoleRepository;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserSeedService implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Value("${ADMIN_USERNAME}")
    private String username;

    @Value("${ADMIN_PASSWORD}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
    }

    private void seedAdminUser() {
        try {
            UserRole adminRole = new UserRole("ADMIN");
            UserRole providerRole = new UserRole("PROVIDER");
            UserRole clientRole = new UserRole("CLIENT");

            // seed all roles
            roleRepository.saveAll(Arrays.asList(adminRole, providerRole, clientRole));
        } catch (Exception ex) {}

        try{
            // seed admin user with roles ['admin']
            List<UserRole> adminRoles = Arrays.asList(roleRepository.findDistinctByRoleName("admin").get());
            User admin = new User(username, passwordEncoder.encode(password), true, Generators.timeBasedGenerator().generate().toString());
            admin.setRoles(adminRoles);
            repository.save(admin);

            // seed admin user with roles ['admin']
            List<UserRole> clientRoles = Arrays.asList(roleRepository.findDistinctByRoleName("client").get());
            User client = new User("client", passwordEncoder.encode("client"), true, Generators.timeBasedGenerator().generate().toString());
            client.setRoles(clientRoles);
            repository.save(client);
        } catch(Exception e){}
    }
}
