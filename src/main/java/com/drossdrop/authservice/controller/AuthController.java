package com.drossdrop.authservice.controller;

import com.drossdrop.authservice.dto.AuthRequest;
import com.drossdrop.authservice.dto.UserCredentialResponse;
import com.drossdrop.authservice.entity.UserCredential;
import com.drossdrop.authservice.service.AuthService;
import com.drossdrop.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService service;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user) {
        return service.saveUser(user);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserCredentialResponse getUserById(@PathVariable Integer id) {
        return service.getUserById(id);
    }

    @PostMapping("/tos")
    @ResponseStatus(HttpStatus.OK)
    public String acceptTOS(@RequestParam("accept") String accept, @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.substring(7);
        String subject = jwtService.getSubjectFromToken(jwtToken);

        return service.acceptTOS(accept, subject);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@PathVariable Integer id) {
        try {
            service.deleteUserById(id);
        } catch (Exception e) {
            return String.format("User with id %d does not exist", id);
        }
        return String.format("User with id %d is deleted", id);
    }
}
