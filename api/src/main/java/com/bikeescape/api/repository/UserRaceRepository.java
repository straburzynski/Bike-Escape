package com.bikeescape.api.repository;

import com.bikeescape.api.model.RaceStatus;
import com.bikeescape.api.model.userrace.UserRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRaceRepository extends JpaRepository<UserRace, Long> {

    @Query(value = "SELECT * FROM UserRace WHERE UserId = ?1 AND RaceId = ?2", nativeQuery = true)
    UserRace findUserRaceByUserIdAndRaceId(@Param("userId") Long userId, @Param("raceId") String raceId);

    @Query(value = "SELECT * FROM UserRace WHERE UserId = ?1 AND RaceStatus <> 'IN_PROGRESS'", nativeQuery = true)
    List<UserRace> findUserRaceByUserId(@Param("userId") Long userId);

    Long countByRaceStatus(RaceStatus raceStatus);

}
