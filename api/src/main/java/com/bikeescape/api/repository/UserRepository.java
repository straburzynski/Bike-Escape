package com.bikeescape.api.repository;

import com.bikeescape.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email=:usernameOrEmail")
    User findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    User findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE removed=FALSE ORDER BY registerdate DESC LIMIT ?1", nativeQuery = true)
    List<User> findLastRegisteredUsers(@Param("limit") int limit);

    @Query(value = "SELECT * FROM users WHERE removed=TRUE ORDER BY removedate DESC LIMIT ?1", nativeQuery = true)
    List<User> findLastRemovedUsers(@Param("limit") int limit);

    @Query("SELECT COUNT(u) FROM User u WHERE u.removed=:removed")
    Long getUsersCountByStatusRemoved(@Param("removed") boolean removed);

    @Query("SELECT COUNT(u) FROM User u")
    Long getAllUsersCount();
}
