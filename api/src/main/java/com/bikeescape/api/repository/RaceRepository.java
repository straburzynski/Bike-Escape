package com.bikeescape.api.repository;

import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.statistics.RaceStatistic;
import com.bikeescape.api.model.statistics.RaceValueStatistic;
import com.bikeescape.api.model.statistics.UserRaceStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, String> {

    Race findOne(String id);

    List<Race> findRacesByNameContaining(String name);

    @Query("SELECT r FROM Race r WHERE r.user.id = :authorId")
    List<Race> findRacesByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT r FROM Race r " +
            "WHERE r.isPublic = TRUE AND r.isActive = TRUE " +
            "AND LOWER(r.name) LIKE LOWER(concat('%', :name, '%'))" +
            "AND LOWER(r.city) LIKE LOWER(concat('%', :city, '%'))" +
            "AND LOWER(r.difficulty.name) LIKE LOWER(concat('%', :difficulty, '%'))" +
            "")
    Page<Race> findRacesByFilters(@Param("name") String name,
                                  @Param("city") String city,
                                  @Param("difficulty") String difficulty,
                                  Pageable pageable);

    @Query("SELECT NEW com.bikeescape.api.model.statistics.UserRaceStatistic(u.email, u.firstName, count(r)) " +
            "FROM Race r INNER JOIN r.user u GROUP BY r.user.Id, u.email, u.firstName ORDER BY COUNT(r) DESC")
    List<UserRaceStatistic> findTopUsersCreatedRaces();

    @Query("SELECT NEW com.bikeescape.api.model.statistics.UserRaceStatistic(u.email, u.firstName, count(ur)) " +
            "FROM UserRace ur INNER JOIN ur.user u GROUP BY ur.user.Id, u.email, u.firstName ORDER BY COUNT(ur) DESC")
    List<UserRaceStatistic> findTopUsersRacesDone();

    @Query("SELECT NEW com.bikeescape.api.model.statistics.RaceStatistic(r.name, r.city, count(ur)) " +
            "FROM UserRace ur INNER JOIN ur.race r GROUP BY ur.race.id, r.name, r.city ORDER BY COUNT(ur) DESC")
    List<RaceStatistic> findTopRacesDone();

    @Query("SELECT NEW com.bikeescape.api.model.statistics.RaceValueStatistic(r.city, count(r)) " +
            "FROM Race r GROUP BY r.city ORDER BY COUNT(r) DESC")
    List<RaceValueStatistic> findTopCityRaces();

    Long countByIsActive(boolean isActive);

    Long countByIsPublic(boolean isPublic);
}
