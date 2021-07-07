package com.bikeescape.api.controller;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.exceptions.UserHasNoPermissionException;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.dto.RaceDTO;
import com.bikeescape.api.model.dto.RaceHeader;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.security.AuthService;

import com.bikeescape.api.service.ImageService;
import com.bikeescape.api.service.RaceCheckpointService;
import com.bikeescape.api.service.RaceService;
import com.bikeescape.api.util.UserPermissionFilter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static com.bikeescape.api.util.Helper.*;

@Log
@CrossOrigin
@RestController
@RequestMapping("race")
public class RaceController {

    private final RaceService raceService;
    private final RaceCheckpointService raceCheckpointService;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final UserPermissionFilter userPermissionFilter;
    private final AuthService authService;

    @Autowired
    public RaceController(RaceService raceService, RaceCheckpointService raceCheckpointService, ModelMapper modelMapper, ImageService imageService, UserPermissionFilter userPermissionFilter, AuthService authService) {
        this.raceService = raceService;
        this.raceCheckpointService = raceCheckpointService;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.userPermissionFilter = userPermissionFilter;
        this.authService = authService;
    }

    // -------------------------- GET RACES --------------------------

    @ApiOperation(value = "Get all races")
    @GetMapping
    public ResponseEntity<List<Race>> getAllRaces() {
        List<Race> races = raceService.findAll();
        if (races == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(races, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all races by author Id")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Race>> getAllRacesByAuthorId(@PathVariable Long authorId) {
        List<Race> races = raceService.findRacesByAuthorId(authorId);
        if (races == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(races, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all logged in user races")
    @GetMapping("/user")
    public ResponseEntity<List<Race>> getLoggedInUserRaces(HttpServletRequest request) {
        Long userId = userPermissionFilter.getUserIdFromRequest(request);
        List<Race> races = raceService.findRacesByAuthorId(userId);
        if (races == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(races, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all races header")
    @GetMapping("/header")
    public ResponseEntity<List<RaceHeader>> getAllRacesHeader() {
        List<Race> races = raceService.findAll();
        if (races == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<RaceHeader> raceHeaders = races.stream()
                .map(race -> new RaceHeader(race.getId(), race.getName(), race.getDescription(),
                        race.getCity(), race.getRaceType().getName(), race.getEstimatedTime().toString(),
                        race.getDifficulty().getName(), race.getCreated().toString()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(raceHeaders, HttpStatus.OK);
    }

    @ApiOperation(value = "Get race by Id")
    @GetMapping("/{raceId}")
    public ResponseEntity<Race> getRaceById(@PathVariable String raceId) {
        Race race = raceService.findOne(raceId);
        if (race == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(race, HttpStatus.OK);
    }

    // -------------------------- FILTERING WITH PAGINATION --------------------------

    @ApiOperation(value = "Get races list by filters")
    @GetMapping("/filters")
    public Page<RaceHeader> getRacesByFilters(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "city", required = false, defaultValue = "") String city,
            @RequestParam(value = "difficulty", required = false, defaultValue = "") String difficulty,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
            @RequestParam(value = "column", required = false, defaultValue = "created") String column) {
        PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.fromString(direction), column);
        Page<Race> races = raceService.findRacesByFilters(name, city, difficulty, pageRequest);
        return races.map(race -> modelMapper.map(race, RaceHeader.class));
    }

    // -------------------------- USER - WEB PERMISSIONS --------------------------

    @ApiOperation(value = "Create new race")
    @PostMapping()
    public ResponseEntity<?> createRace(@RequestBody RaceDTO raceDTO,
                                        HttpServletRequest request) throws Exception {
        Long userId = userPermissionFilter.getUserIdFromRequest(request);
        raceDTO.setAuthorId(userId);
        Race race = raceService.createRace(raceDTO);
        if (!isNullOrEmpty(raceDTO.getRaceImage()))
            imageService.saveRaceImage(raceDTO.getRaceImage(), race.getId());
        if (!isNullOrEmpty(raceDTO.getSummaryImage()))
            imageService.saveSummaryImage(raceDTO.getSummaryImage(), race.getId());
        if (!isNullOrEmpty(raceDTO.getFailImage()))
            imageService.saveFailImage(raceDTO.getFailImage(), race.getId());
        User user = authService.getLoggedUser(request);
        log.info("Race created, user: " + user.toString() + ", race: " + race.toString());
        return ResponseEntity.ok("Race created");
    }

    @ApiOperation(value = "Edit race by Id")
    @PutMapping("/{raceId}")
    public ResponseEntity<?> saveRace(@RequestBody RaceDTO raceDTO,
                                      @PathVariable String raceId,
                                      HttpServletRequest request) throws ParseException, IOException, RaceValidationException {
        if (!userPermissionFilter.canUserChangeRace(raceId, request)) {
            throw new UserHasNoPermissionException("No permission");
        }
        raceDTO.setId(raceId);
        raceService.saveRace(raceDTO);
        if (!isNullOrEmpty(raceDTO.getRaceImage()))
            imageService.saveRaceImage(raceDTO.getRaceImage(), raceId);
        if (!isNullOrEmpty(raceDTO.getSummaryImage()))
            imageService.saveSummaryImage(raceDTO.getSummaryImage(), raceId);
        if (!isNullOrEmpty(raceDTO.getFailImage()))
            imageService.saveFailImage(raceDTO.getFailImage(), raceId);
        User user = authService.getLoggedUser(request);
        log.info("Race edited, user: " + user.toString() + ", race id: " + raceDTO.toString());
        return ResponseEntity.ok("Race saved");
    }

    @ApiOperation(value = "Delete race with checkpoints by Id")
    @DeleteMapping("/{raceId}")
    public ResponseEntity<?> deleteRaceWithCheckpoints(@PathVariable String raceId,
                                                       HttpServletRequest request) {
        if (!userPermissionFilter.canUserChangeRace(raceId, request)) {
            throw new UserHasNoPermissionException("No permission");
        }
        raceCheckpointService.deleteAllCheckpointsByRaceId(raceId);
        raceService.deleteRace(raceId);
        imageService.deleteRaceImagesFolder(raceId);
        User user = authService.getLoggedUser(request);
        log.info("Race deleted, user: " + user.toString() + ", race: " + raceId);
        return ResponseEntity.ok("Race with checkpoints deleted");
    }

}
