package com.bikeescape.api.controller;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.exceptions.UserHasNoPermissionException;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.RaceCheckpoint;
import com.bikeescape.api.model.dto.RaceCheckpointDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.bikeescape.api.util.Helper.isNullOrEmpty;

@Log
@CrossOrigin
@RestController
@RequestMapping("race")
public class RaceCheckpointController {

    private final RaceService raceService;
    private final RaceCheckpointService raceCheckpointService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;
    private final UserPermissionFilter userPermissionFilter;
    private final AuthService authService;

    @Autowired
    public RaceCheckpointController(RaceService raceService, RaceCheckpointService raceCheckpointService, ImageService imageService, ModelMapper modelMapper, UserPermissionFilter userPermissionFilter, AuthService authService) {
        this.raceService = raceService;
        this.raceCheckpointService = raceCheckpointService;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
        this.userPermissionFilter = userPermissionFilter;
        this.authService = authService;
    }

    // -------------------------- USER - GET CHECKPOINTS --------------------------

    @ApiOperation(value = "Get all checkpoints details by race Id")
    @GetMapping("/{raceId}/checkpoint")
    public ResponseEntity<List<RaceCheckpoint>> getAllRaceCheckpointsByRaceId(@PathVariable String raceId) {
        Race race = raceService.findOne(raceId);
        if (race == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<RaceCheckpoint> checkpoints = raceCheckpointService.findByRaceId(race.getId());
        return new ResponseEntity<>(checkpoints, HttpStatus.OK);
    }

    @ApiOperation(value = "Get specific checkpoint details by race Id and number")
    @GetMapping("/{raceId}/checkpoint/{checkpointNumber}")
    public ResponseEntity<RaceCheckpoint> getRaceCheckpointByRaceId(@PathVariable String raceId,
                                                                    @PathVariable Long checkpointNumber) {
        RaceCheckpoint raceCheckpoint = raceCheckpointService.findByRaceIdAndNumber(raceId, checkpointNumber);
        if (raceCheckpoint == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(raceCheckpoint, HttpStatus.OK);
    }

    @ApiOperation(value = "Get checkpoint by Id")
    @GetMapping("/checkpoint/{checkpointId}")
    public ResponseEntity<RaceCheckpointDTO> getRaceCheckpointByID(@PathVariable String checkpointId) {
        RaceCheckpoint raceCheckpoint = raceCheckpointService.findOne(checkpointId);
        RaceCheckpointDTO raceCheckpointDTO = modelMapper.map(raceCheckpoint, RaceCheckpointDTO.class);
        if (raceCheckpointDTO == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(raceCheckpointDTO, HttpStatus.OK);
    }

    // -------------------------- USER - WEB PERMISSIONS --------------------------

    @ApiOperation(value = "Create new checkpoint")
    @PostMapping("{raceId}/checkpoint")
    public ResponseEntity<?> createRaceCheckpoint(@RequestBody RaceCheckpointDTO raceCheckpointDTO,
                                                  @PathVariable String raceId,
                                                  HttpServletRequest request) throws ParseException, IOException, RaceValidationException {
        if (!userPermissionFilter.canUserChangeRace(raceId, request)) {
            throw new UserHasNoPermissionException("No permission");
        }
        raceCheckpointDTO.setRaceId(raceId);
        RaceCheckpoint raceCheckpoint = raceCheckpointService.createRaceCheckpoint(raceCheckpointDTO);
        if (!isNullOrEmpty(raceCheckpointDTO.getCheckpointImage())) {
            imageService.saveCheckpointImage(
                    raceCheckpointDTO.getCheckpointImage(), raceId, raceCheckpoint.getId()
            );
        }
        User user = authService.getLoggedUser(request);
        log.info("Race checkpoint created, user: " + user.toString() + ", checkpoint: " + raceCheckpoint.toString());
        return ResponseEntity.ok("Race checkpoint created");
    }

    @ApiOperation(value = "Edit checkpoint by raceId and checkpoint number")
    @PutMapping("/{raceId}/checkpoint/{checkpointNumber}")
    public ResponseEntity<?> saveRaceCheckpointByNumber(@RequestBody RaceCheckpointDTO raceCheckpointDTO,
                                                        @PathVariable String raceId,
                                                        @PathVariable Long checkpointNumber,
                                                        HttpServletRequest request) throws RaceValidationException {
        if (!userPermissionFilter.canUserChangeRace(raceId, request)) {
            throw new UserHasNoPermissionException("No permission");
        }
        raceCheckpointDTO.setRaceId(raceId);
        raceCheckpointDTO.setNumber(checkpointNumber);
        raceCheckpointService.saveRaceCheckpoint(raceCheckpointDTO);
        User user = authService.getLoggedUser(request);
        log.info("Race checkpoint edited, user: " + user.toString());
        return ResponseEntity.ok("Race checkpoint saved");
    }

    @ApiOperation(value = "Edit checkpoint by Id")
    @PutMapping("/checkpoint/{checkpointId}")
    public ResponseEntity<?> saveRaceCheckpointById(@RequestBody RaceCheckpointDTO raceCheckpointDTO,
                                                    @PathVariable String checkpointId,
                                                    HttpServletRequest request) throws IOException, RaceValidationException {
        RaceCheckpoint raceCheckpoint = raceCheckpointService.findOne(checkpointId);
        if (!userPermissionFilter.canUserChangeRace(raceCheckpoint.getRace().getId(), request)) {
            throw new UserHasNoPermissionException("No permission");
        }
        raceCheckpointDTO.setId(checkpointId);
        raceCheckpointService.saveRaceCheckpoint(raceCheckpointDTO);
        if (!isNullOrEmpty(raceCheckpointDTO.getCheckpointImage())) {
            imageService.saveCheckpointImage(
                    raceCheckpointDTO.getCheckpointImage(), raceCheckpointDTO.getRaceId(), checkpointId
            );
        }
        User user = authService.getLoggedUser(request);
        log.info("Race checkpoint edited, user: " + user.toString() + ", checkpoint: " + raceCheckpoint.toString());
        return ResponseEntity.ok("Race checkpoint saved");
    }

    @ApiOperation(value = "Delete checkpoint by Id")
    @DeleteMapping("/checkpoint/{checkpointId}")
    public ResponseEntity<?> deleteRaceCheckpoint(@PathVariable String checkpointId,
                                                  HttpServletRequest request) throws IOException, RaceValidationException {
        RaceCheckpoint raceCheckpoint = raceCheckpointService.findOne(checkpointId);
        if (!userPermissionFilter.canUserChangeRace(raceCheckpoint.getRace().getId(), request)) {
            throw new UserHasNoPermissionException("No permission");
        }
        raceCheckpointService.deleteRaceCheckpoint(checkpointId);
        imageService.deleteCheckpointImage(raceCheckpoint.getRace().getId(), checkpointId);
        User user = authService.getLoggedUser(request);
        log.info("Race checkpoint deleted, user: " + user.toString() + ", checkpoint: " + raceCheckpoint.toString());
        return ResponseEntity.ok("Race checkpoint deleted");
    }

}
