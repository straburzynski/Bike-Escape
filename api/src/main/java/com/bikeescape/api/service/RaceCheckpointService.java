package com.bikeescape.api.service;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.model.RaceCheckpoint;
import com.bikeescape.api.model.dto.RaceCheckpointDTO;

import java.text.ParseException;
import java.util.List;

public interface RaceCheckpointService {

    List<RaceCheckpoint> findAll();

    RaceCheckpoint findOne(String checkpointId);

    List<RaceCheckpoint> findByRaceId(String id);

    RaceCheckpoint findByRaceIdAndNumber(String raceId, Long number);

    void saveRaceCheckpoint(RaceCheckpointDTO raceCheckpointDTO) throws RaceValidationException;

    RaceCheckpoint createRaceCheckpoint(RaceCheckpointDTO raceCheckpointDTO) throws ParseException, RaceValidationException;

    void deleteRaceCheckpoint(String checkpointId) throws RaceValidationException;

    void deleteAllCheckpointsByRaceId(String raceId);

}
