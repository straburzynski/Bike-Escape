package com.bikeescape.api.controller;

import com.bikeescape.api.model.statistics.Statistics;
import com.bikeescape.api.service.StatisticsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @ApiOperation(value = "Get all statistics data for admin")
    @GetMapping()
    public ResponseEntity<?> getStatistics() {
        Statistics statistics = statisticsService.getStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

}