package com.bikeescape.api.converter;

import com.bikeescape.api.model.RaceStatus;
import com.bikeescape.api.model.userrace.UserRace;
import com.bikeescape.api.model.userrace.UserRaceRankingTO;
import com.bikeescape.api.model.userrace.UserRaceTO;
import com.bikeescape.api.service.RaceService;
import com.bikeescape.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRaceConverter {

    private final UserService userService;
    private final RaceService raceService;

    @Autowired
    public UserRaceConverter(UserService userService, RaceService raceService) {
        this.userService = userService;
        this.raceService = raceService;
    }

    public UserRace convertUserRaceTOToUserRace(UserRaceTO userRaceTO) throws Exception {
        return UserRace.builder()
                .user(userService.findById(userRaceTO.getUserId()))
                .race(raceService.findOne(userRaceTO.getRaceId()))
                .raceStatus(RaceStatus.valueOf(userRaceTO.getRaceStatus().toUpperCase()))
                .totalTime(userRaceTO.getTotalTime())
                .build();
    }

    public UserRaceTO convertUserRaceToUserRaceTO(UserRace userRace) {
        return UserRaceTO.builder()
                .userId(userRace.getUser().getId())
                .raceId(userRace.getRace().getId())
                .totalTime(userRace.getTotalTime())
                .raceStatus(userRace.getRaceStatus().getType())
                .build();
    }

    private UserRaceRankingTO convertUserRaceToUserRaceRankingTO(UserRace userRace) {
        return UserRaceRankingTO.builder()
                .userId(userRace.getUser().getId())
                .userName(userRace.getUser().getFirstName())
                .raceId(userRace.getRace().getId())
                .raceName(userRace.getRace().getName())
                .city(userRace.getRace().getCity())
                .raceStatus(userRace.getRaceStatus().getType())
                .totalTime(userRace.getTotalTime())
                .finishDate(userRace.getFinishDate().toString())
                .build();
    }

    public List<UserRaceRankingTO> convertUserRaceListToUserRaceRankingTOList(List<UserRace> userRaceList) {
        List<UserRaceRankingTO> userRaceRankingTOList = new ArrayList<>();
        for (UserRace userRace: userRaceList) {
            UserRaceRankingTO userRaceRankingTO = convertUserRaceToUserRaceRankingTO(userRace);
            userRaceRankingTOList.add(userRaceRankingTO);
        }
        return userRaceRankingTOList;
    }

}
