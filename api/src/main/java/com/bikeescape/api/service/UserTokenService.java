package com.bikeescape.api.service;

import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.UserToken;

public interface UserTokenService {

    UserToken findByUserId(Long userId);

    UserToken findByToken(String token);

    void saveUserToken(User user, String token);

    UserToken deleteUserToken(UserToken userToken);

}
