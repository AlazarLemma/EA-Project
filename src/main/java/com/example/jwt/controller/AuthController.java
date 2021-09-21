package com.example.jwt.controller;

import com.example.jwt.domain.User;
import com.example.jwt.domain.UserRole;
import com.example.jwt.service.AuthUserDetailService;
import com.example.jwt.service.UserAdapterService;
import com.example.jwt.service.UserService;
import com.example.jwt.service.dto.AuthRequestDTO;
import com.example.jwt.service.dto.AuthResponseDTO;
import com.example.jwt.service.dto.AuthUserSubject;
import com.example.jwt.service.dto.UserDto;
import com.example.jwt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdapterService userAdapterService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequestDTO dto) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<String>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        final User user = userService.loadUserByUsername(dto.getUsername());
        final AuthUserSubject subject = userAdapterService.fromUserToAuthSubject(user);
        String jwt = jwtUtil.generateToken(subject);

        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        List<UserRole> roles = userService.getRoles();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto dto) throws Exception {
        User user = userService.loadUserByUsername(dto.getUsername());

        if (user != null) {
            return new ResponseEntity<String>("user already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        UserDto result = userService.registerUser(dto);
        return ResponseEntity.ok(result);
    }
}
