package com.example.jwt.controller;

import com.example.jwt.domain.User;
import com.example.jwt.service.AuthUserDetailService;
import com.example.jwt.service.UserAdapterService;
import com.example.jwt.service.UserService;
import com.example.jwt.service.dto.AuthRequestDTO;
import com.example.jwt.service.dto.AuthResponseDTO;
import com.example.jwt.service.dto.AuthUserSubject;
import com.example.jwt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequestDTO dto) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }

        final User user = userService.loadUserByUsername(dto.getUsername());
        final AuthUserSubject subject = userAdapterService.fromUserToAuthSubject(user);
        String jwt = jwtUtil.generateToken(subject);

        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
}
