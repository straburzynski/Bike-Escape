package com.bikeescape.api.controller;

import com.bikeescape.api.converter.UserRaceConverter;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.model.userrace.UserRace;
import com.bikeescape.api.model.userrace.UserRaceRankingTO;
import com.bikeescape.api.model.userrace.UserRaceTO;
import com.bikeescape.api.security.AuthService;
import com.bikeescape.api.service.UserRaceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("race/history")
public class RaceHistoryController {

    private final AuthService authService;
    private final UserRaceService userRaceService;
    private final UserRaceConverter userRaceConverter;

    @Autowired
    public RaceHistoryController(AuthService authService, UserRaceService userRaceService, UserRaceConverter userRaceConverter) {
        this.authService = authService;
        this.userRaceService = userRaceService;
        this.userRaceConverter = userRaceConverter;
    }

    @ApiOperation(value = "Create new race entry")
    @PostMapping()
    public ResponseEntity<?> createNewRaceEntry(@RequestBody UserRaceTO userRaceTO,
                                                HttpServletRequest request) throws Exception {
        User user = authService.getLoggedUser(request);
        userRaceTO.setUserId(user.getId());
        UserRace userRace = userRaceService.saveNewUserRace(userRaceTO);
        if (userRace == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(userRaceConverter.convertUserRaceToUserRaceTO(userRace), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Update race entry")
    @PutMapping()
    public ResponseEntity<?> updateRaceEntry(@RequestBody UserRaceTO userRaceTO,
                                             HttpServletRequest request) throws Exception {
        User user = authService.getLoggedUser(request);
        userRaceTO.setUserId(user.getId());
        UserRace userRace = userRaceService.updateUserRace(userRaceTO);
        if (userRace == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(userRaceConverter.convertUserRaceToUserRaceTO(userRace), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Get all races history")
    @GetMapping()
    public ResponseEntity<?> getAllRacesHistory() {
        List<UserRace> userRaceList = userRaceService.findAll();
        if (userRaceList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserRaceRankingTO> userRaceRankingTOList = userRaceConverter.convertUserRaceListToUserRaceRankingTOList(userRaceList);
        return new ResponseEntity<>(userRaceRankingTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all races history by user")
    @GetMapping("/user")
    public ResponseEntity<?> getAllRacesHistoryByUser(HttpServletRequest request) {
        User user = authService.getLoggedUser(request);
        List<UserRace> userRaceList = userRaceService.findByUserId(user.getId());
        if (userRaceList == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserRaceRankingTO> userRaceRankingTOList = userRaceConverter.convertUserRaceListToUserRaceRankingTOList(userRaceList);
        return new ResponseEntity<>(userRaceRankingTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "Get race history by userId and raceId")
    @GetMapping("{userId}/{raceId}")
    public ResponseEntity<UserRace> getUserRaceHistory(@PathVariable Long userId, @PathVariable String raceId) {
        UserRace userRace = userRaceService.findByUserIdAndRaceId(userId, raceId);
        if (userRace == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userRace, HttpStatus.OK);
    }

    @ApiOperation(value = "Has user already played race")
    @GetMapping("{raceId}")
    public ResponseEntity<Boolean> hasUserDonePlayed(@PathVariable String raceId,
                                                     HttpServletRequest request) {
        User user = authService.getLoggedUser(request);
        boolean isAlreadyPlayed = userRaceService.isAlreadyPlayed(user.getId(), raceId);
        return new ResponseEntity<>(isAlreadyPlayed, HttpStatus.OK);
    }

}
