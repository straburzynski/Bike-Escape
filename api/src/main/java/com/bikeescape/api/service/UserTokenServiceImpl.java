package com.bikeescape.api.service;

import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.UserToken;
import com.bikeescape.api.repository.UserTokenRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserTokenServiceImpl(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public UserToken findByUserId(Long userId) {
        return userTokenRepository.findByUserId(userId);
    }

    @Override
    public UserToken findByToken(String token) {
        return this.userTokenRepository.findByToken(token);
    }

    @Override
    public void saveUserToken(User user, String token) {
        UserToken userToken = new UserToken(user.getId(), token);
        this.userTokenRepository.save(userToken);
        log.info("User token created: " + userToken.getToken() + " for user " + user.toString());
    }

    @Override
    public UserToken deleteUserToken(UserToken userToken) {
        this.userTokenRepository.delete(userToken);
        log.info("User token deleted: " + userToken.getToken() + " for user " + userToken.getUserId());
        return userToken;
    }

}

