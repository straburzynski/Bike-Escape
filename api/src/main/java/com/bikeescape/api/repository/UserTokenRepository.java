package com.bikeescape.api.repository;

import com.bikeescape.api.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findByUserId(Long userId);

    UserToken findByToken(String token);

}
