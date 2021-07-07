package com.bikeescape.api.service;

import com.bikeescape.api.converter.UserRaceConverter;
import com.bikeescape.api.exceptions.RaceIsAlreadyDoneException;
import com.bikeescape.api.model.RaceStatus;
import com.bikeescape.api.model.userrace.UserRace;
import com.bikeescape.api.model.userrace.UserRaceTO;
import com.bikeescape.api.repository.UserRaceRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Log
@Service
public class UserRaceServiceImpl implements UserRaceService {

    private final UserRaceRepository userRaceRepository;
    private final UserRaceConverter userRaceConverter;

    @Autowired
    public UserRaceServiceImpl(UserRaceRepository userRaceRepository, UserRaceConverter userRaceConverter) {
        this.userRaceRepository = userRaceRepository;
        this.userRaceConverter = userRaceConverter;
    }

    @Override
    public boolean isAlreadyPlayed(Long userId, String raceId) {
        return userRaceRepository.findUserRaceByUserIdAndRaceId(userId, raceId) != null;
    }

    @Override
    public UserRace findByUserIdAndRaceId(Long userId, String raceId) {
        return userRaceRepository.findUserRaceByUserIdAndRaceId(userId, raceId);
    }

    @Override
    public UserRace saveNewUserRace(UserRaceTO userRaceTO) throws Exception {
        if (isAlreadyPlayed(userRaceTO.getUserId(), userRaceTO.getRaceId())) {
            throw new RaceIsAlreadyDoneException("Race is already done");
        }
        UserRace userRace = userRaceConverter.convertUserRaceTOToUserRace(userRaceTO);
        userRace.setFinishDate(Calendar.getInstance().getTime());
        log.info("User race created: " + userRace.toString());
        return userRaceRepository.save(userRace);
    }

    @Override
    public UserRace updateUserRace(UserRaceTO userRaceTO) {
        UserRace userRace = findByUserIdAndRaceId(userRaceTO.getUserId(), userRaceTO.getRaceId());
        userRace.setRaceStatus(RaceStatus.valueOf(userRaceTO.getRaceStatus().toUpperCase()));
        userRace.setTotalTime(userRaceTO.getTotalTime());
        userRace.setFinishDate(Calendar.getInstance().getTime());
        log.info("User race updated: " + userRace.toString());
        return userRaceRepository.save(userRace);
    }

    @Override
    public List<UserRace> findAll() {
        return userRaceRepository.findAll();
    }

    @Override
    public List<UserRace> findByUserId(Long userId) {
        return userRaceRepository.findUserRaceByUserId(userId);
    }

    @Override
    public Long countAllUserRaces() {
        return userRaceRepository.count();
    }

    @Override
    public Long countByRaceStatus(RaceStatus raceStatus) {
        return userRaceRepository.countByRaceStatus(raceStatus);
    }

}
