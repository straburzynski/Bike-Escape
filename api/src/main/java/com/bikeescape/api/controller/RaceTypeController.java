package com.bikeescape.api.controller;

import com.bikeescape.api.model.RaceType;
import com.bikeescape.api.service.RaceTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("race/type")
public class RaceTypeController {

    private final RaceTypeService raceTypeService;

    @Autowired
    public RaceTypeController(RaceTypeService raceTypeService) {
        this.raceTypeService = raceTypeService;
    }

    @ApiOperation(value = "Get all race types")
    @GetMapping()
    public ResponseEntity<List<RaceType>> getAllRaceTypes() {
        List<RaceType> raceType = raceTypeService.findAll();
        if (raceType == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(raceType, HttpStatus.OK);
    }

    @ApiOperation(value = "Get race type by Id")
    @GetMapping("/{typeId}")
    public ResponseEntity<RaceType> getRaceTypeById(@PathVariable int typeId) {
        RaceType raceType = raceTypeService.findOne(typeId);
        if (raceType == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(raceType, HttpStatus.OK);
    }
}
