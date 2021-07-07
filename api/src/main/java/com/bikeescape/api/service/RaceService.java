package com.bikeescape.api.service;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.dto.RaceDTO;
import com.bikeescape.api.model.statistics.RaceStatistic;
import com.bikeescape.api.model.statistics.RaceValueStatistic;
import com.bikeescape.api.model.statistics.UserRaceStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.util.List;

public interface RaceService {

    Race findOne(String id);

    List<Race> findAll();

    List<Race> findRacesByNameContaining(String name);

    List<Race> findRacesByAuthorId(Long authorId);

    Page<Race> findRacesByFilters(String name, String city, String difficulty, PageRequest pageRequest);

    Page<Race> findAll(PageRequest pageRequest);

    void saveRace(RaceDTO raceDTO) throws ParseException, RaceValidationException;

    void save(Race race) throws RaceValidationException;

    Race createRace(RaceDTO raceDTO) throws Exception;

    void deleteRace(String raceId);

    List<UserRaceStatistic> findTopUsersCreatedRaces(int limit);

    List<UserRaceStatistic> findTopUsersRacesDone(int limit);

    List<RaceStatistic> findTopRacesDone(int limit);

    List<RaceValueStatistic> findTopCityRaces(int limit);

    Long totalRacesCount();

    Long totalRacesActive(boolean isActive);

    Long totalRacesPublic(boolean isPublic);

}
