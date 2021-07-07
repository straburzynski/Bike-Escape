package com.bikeescape.api.service;

import com.bikeescape.api.model.RaceStatus;
import com.bikeescape.api.model.userrace.UserRace;
import com.bikeescape.api.model.userrace.UserRaceTO;

import java.util.List;

public interface UserRaceService {

    boolean isAlreadyPlayed(Long userId, String raceId);

    UserRace findByUserIdAndRaceId(Long userId, String raceId);

    UserRace saveNewUserRace(UserRaceTO userRaceTO) throws Exception;

    UserRace updateUserRace(UserRaceTO userRaceTO) throws Exception;

    List<UserRace> findAll();

    List<UserRace> findByUserId(Long userId);

    Long countAllUserRaces();

    Long countByRaceStatus(RaceStatus raceStatus);

}
