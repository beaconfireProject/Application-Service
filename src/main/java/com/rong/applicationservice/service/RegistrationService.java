package com.rong.applicationservice.service;

import com.rong.applicationservice.dao.UserDao;
import com.rong.applicationservice.domain.User;
import com.rong.applicationservice.dto.request.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final UserDao userDao;

    public RegistrationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int addUser(RegistrationRequest registrationRequest) {
        User newUser = User.builder()
                .email(registrationRequest.getEmail())
                .username(registrationRequest.getUsername())
                .password(new BCryptPasswordEncoder().encode(registrationRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(1)
                .build();
        int id = userDao.add(newUser);
        return id;
    }
}
