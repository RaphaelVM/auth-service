package com.drossdrop.authservice.service;

import com.drossdrop.authservice.dto.UserCredentialResponse;
import com.drossdrop.authservice.dto.UserRabbitMQ;
import com.drossdrop.authservice.entity.UserCredential;
import com.drossdrop.authservice.rabbitmq.RabbitMQProducer;
import com.drossdrop.authservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    public String saveUser(UserCredential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        UserRabbitMQ mappedUser = mapToUserRabbitMQ(credential);
        rabbitMQProducer.sendUser(mappedUser);
        return "User added to the system";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(repository.findByUsername(username).get());
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public UserCredentialResponse getUserById(Integer id) {
        UserCredential userCredential = repository.findById(id).orElseThrow();
        return mapToUserCredentialResponse(userCredential);
    }

    private UserCredentialResponse mapToUserCredentialResponse(UserCredential userCredential) {
        return UserCredentialResponse.builder()
                .id(String.valueOf(userCredential.getId()))
                .username(userCredential.getUsername())
                .email(userCredential.getEmail())
                .roleName(userCredential.getRoleName())
                .build();
    }

    private UserRabbitMQ mapToUserRabbitMQ(UserCredential userCredential) {
        return UserRabbitMQ.builder()
                .id(String.valueOf(userCredential.getId()))
                .username(userCredential.getUsername())
                .email(userCredential.getEmail())
                .build();
    }

    public String acceptTOS(String accept, String subject) {
        if (!accept.equals("true")) {
            return "TOS not accepted";
        }
        UserCredential userCredential = repository.findById(Integer.valueOf(subject)).orElseThrow();
        repository.save(userCredential);
        return String.format("TOS accepted for user %s", userCredential.toString());
    }

    public Boolean deleteUserById(Integer id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
