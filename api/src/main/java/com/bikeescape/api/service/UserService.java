package com.bikeescape.api.service;

import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.user.UserRegistrationTO;
import com.bikeescape.api.model.user.UserResetPasswordTO;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    User findByUsernameOrEmail(String usernameOrEmail);

    User findById(Long id) throws Exception;

    boolean emailExists(String username);

    void verifyEmail(String email);

    User save(UserRegistrationTO userRegistrationTO) throws MessagingException;

    User save(User user);

    List<User> findAll() throws Exception;

    void createResetToken(String email) throws MessagingException;

    User changePasswordWithToken(UserResetPasswordTO userResetPasswordTO);

    void removeUser(Long userId);

    List<User> findLastRegisteredUsers(int limit);

    List<User> findLastRemovedUsers(int limit);

    Long getUsersCountByStatusRemoved(boolean removed);

    Long getAllUsersCount();

}
