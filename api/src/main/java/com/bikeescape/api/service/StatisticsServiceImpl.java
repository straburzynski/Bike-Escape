package com.bikeescape.api.service;

import com.bikeescape.api.model.RaceStatus;
import com.bikeescape.api.model.statistics.RaceStatistic;
import com.bikeescape.api.model.statistics.RaceValueStatistic;
import com.bikeescape.api.model.statistics.Statistics;
import com.bikeescape.api.model.statistics.UserRaceStatistic;
import com.bikeescape.api.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserService userService;
    private final RaceService raceService;
    private final UserRaceService userRaceService;

    @Autowired
    public StatisticsServiceImpl(UserService userService, RaceService raceService, UserRaceService userRaceService) {
        this.userService = userService;
        this.raceService = raceService;
        this.userRaceService = userRaceService;
    }

    @Override
    public Statistics getStatistics() {
        return Statistics.builder()
                // users
                .lastRegisteredUsers(getLastRegisteredUsers())
                .lastRemovedUsers(getLastRemovedUsers())
                .topUsersCreatedRaces(findTopUsersCreatedRaces())
                .topUsersDoneRaces(findTopUsersRacesDone())
                .totalUsers(getTotalUsersCount())
                .totalActiveUsers(getTotalUserCountByStatusRemoved(false))
                .totalRemovedUsers(getTotalUserCountByStatusRemoved(true))
                // races
                .topRacesDone(findTopRacesDone())
                .topCitiesRaces(findTopCityRaces())
                .totalRaces(totalRacesCount())
                .totalPrivateRaces(findRaceByPublic(false))
                .totalPublicRaces(findRaceByPublic(true))
                .totalActiveRaces(findRaceByActive(true))
                .totalInactiveRaces(findRaceByActive(false))
                // user races
                .totalRacesDone(countTotalRacesDone())
                .totalRacesWon(countUserRacesByRaceStatus(RaceStatus.FINISHED))
                .totalRacesFailed(countUserRacesByRaceStatus(RaceStatus.FAILED))
                .totalRacesInProgress(countUserRacesByRaceStatus(RaceStatus.IN_PROGRESS)).build();
    }

    private List<User> getLastRegisteredUsers() {
        return userService.findLastRegisteredUsers(3);
    }

    private List<User> getLastRemovedUsers() {
        return userService.findLastRemovedUsers(3);
    }

    private Long getTotalUserCountByStatusRemoved(boolean status) {
        return userService.getUsersCountByStatusRemoved(status);
    }

    private Long getTotalUsersCount() {
        return userService.getAllUsersCount();
    }

    private List<UserRaceStatistic> findTopUsersCreatedRaces() {
        return raceService.findTopUsersCreatedRaces(3);
    }

    private List<UserRaceStatistic> findTopUsersRacesDone() {
        return raceService.findTopUsersRacesDone(3);
    }

    private List<RaceStatistic> findTopRacesDone() {
        return raceService.findTopRacesDone(3);
    }

    private List<RaceValueStatistic> findTopCityRaces() {
        return raceService.findTopCityRaces(3);
    }

    private Long totalRacesCount() {
        return raceService.totalRacesCount();
    }

    private Long findRaceByActive(boolean isActive) {
        return raceService.totalRacesActive(isActive);
    }

    private Long findRaceByPublic(boolean isPublic) {
        return raceService.totalRacesPublic(isPublic);
    }

    private Long countTotalRacesDone() {
        return userRaceService.countAllUserRaces();
    }

    private Long countUserRacesByRaceStatus(RaceStatus raceStatus) {
        return userRaceService.countByRaceStatus(raceStatus);
    }

}
