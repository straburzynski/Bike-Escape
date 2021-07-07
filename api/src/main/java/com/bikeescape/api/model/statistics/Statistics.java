package com.bikeescape.api.model.statistics;

import com.bikeescape.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Statistics {

    // users

    private List<User> lastRegisteredUsers;
    private List<User> lastRemovedUsers;
    private List<UserRaceStatistic> topUsersCreatedRaces;
    private List<UserRaceStatistic> topUsersDoneRaces;

    private Long totalUsers;
    private Long totalActiveUsers;
    private Long totalRemovedUsers;

    // races

    private List<RaceStatistic> topRacesDone;
    private List<RaceValueStatistic> topCitiesRaces;

    private Long totalRaces;
    private Long totalPrivateRaces;
    private Long totalPublicRaces;
    private Long totalActiveRaces;
    private Long totalInactiveRaces;

    private Long totalRacesDone;
    private Long totalRacesWon;
    private Long totalRacesFailed;
    private Long totalRacesInProgress;

}
