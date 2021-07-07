package com.bikeescape.api.service;

import com.bikeescape.api.converter.UserConverter;
import com.bikeescape.api.exceptions.TokenNotFoundException;
import com.bikeescape.api.exceptions.UserFoundException;
import com.bikeescape.api.exceptions.UserNotFoundException;
import com.bikeescape.api.model.Authority;
import com.bikeescape.api.model.UserToken;
import com.bikeescape.api.model.email.EmailData;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.user.UserRegistrationTO;
import com.bikeescape.api.model.user.UserResetPasswordTO;
import com.bikeescape.api.repository.RoleRepository;
import com.bikeescape.api.repository.UserRepository;
import com.bikeescape.api.util.RandomStringGenerator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Log
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final UserTokenService userTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final RandomStringGenerator generator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, UserTokenService userTokenService, PasswordEncoder passwordEncoder, UserConverter userConverter, RandomStringGenerator generator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.userTokenService = userTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userConverter = userConverter;
        this.generator = generator;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public void verifyEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new UserFoundException("E-mail already registered");
        }
    }

    @Override
    public User save(UserRegistrationTO userRegistrationTO) {
        User user = userConverter.convertUserRegistrationTOToUser(userRegistrationTO);
        verifyEmail(user.getEmail());
        List<Authority> authorities = new ArrayList<>();
        authorities.add(roleRepository.findByName("ROLE_USER"));
        user.setAuthorities(authorities);
        user.setRegisterDate(Calendar.getInstance().getTime());
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        if (users == null) {
            throw new UserNotFoundException("Users not found");
        }
        return users;
    }

    @Override
    public void createResetToken(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        String token = generator.randomString(5);
        UserToken userToken = userTokenService.findByUserId(user.getId());
        if (userToken != null) {
            sendResetPasswordMail(user, userToken.getToken());
        } else {
            userTokenService.saveUserToken(user, token);
            sendResetPasswordMail(user, token);
        }
    }

    @Override
    public User changePasswordWithToken(UserResetPasswordTO userResetPasswordTO) {
        UserToken userToken = userTokenService.findByToken(userResetPasswordTO.getToken());
        if (userToken == null) {
            throw new TokenNotFoundException("Wrong PIN");
        }
        User user = userRepository.findOne(userToken.getUserId());
        user.setPassword(passwordEncoder.encode(userResetPasswordTO.getPassword()));
        userRepository.save(user);
        userTokenService.deleteUserToken(userToken);
        return user;
    }

    @Override
    public void removeUser(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null || user.isRemoved()) {
            throw new UserNotFoundException("User not found");
        }
        user.setRemoved(true);
        user.setRemoveDate(Calendar.getInstance().getTime());
        userRepository.save(user);
    }

    @Override
    public List<User> findLastRegisteredUsers(int limit) {
        return userRepository.findLastRegisteredUsers(limit);
    }

    @Override
    public List<User> findLastRemovedUsers(int limit) {
        return userRepository.findLastRemovedUsers(limit);
    }

    @Override
    public Long getUsersCountByStatusRemoved(boolean removed) {
        return userRepository.getUsersCountByStatusRemoved(removed);
    }

    @Override
    public Long getAllUsersCount() {
        return userRepository.getAllUsersCount();
    }

    private void sendResetPasswordMail(User user, String token) throws MessagingException {
        EmailData emailData = EmailData.builder()
                .subject("Bike Escape - Reset password")
                .message("User id: " + user.getId() + ", token: " + token + ", e-mail: " + user.getEmail())
                .recipients(new String[]{user.getEmail()})
                .build();
        emailService.sendMessage(emailData);
    }

}
