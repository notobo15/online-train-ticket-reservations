package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.OAuth2Connection;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Repos.OAuth2ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2ConnectionService {

    private final OAuth2ConnectionRepository oAuth2ConnectionRepository;

    public void save(OAuth2Connection connection) {
        oAuth2ConnectionRepository.save(connection);
    }
    public Optional<OAuth2Connection> findByUserAndProvider(User user, String provider) {
        return oAuth2ConnectionRepository.findByUserAndProvider(user, provider);
    }
    public boolean existsByUserAndProvider(User user, String provider) {
        return oAuth2ConnectionRepository.existsByUserAndProvider(user, provider);
    }
}