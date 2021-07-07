package com.bikeescape.api.service;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.model.*;
import com.bikeescape.api.model.AnswerType;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.RaceCheckpoint;
import com.bikeescape.api.model.dto.RaceCheckpointDTO;
import com.bikeescape.api.repository.RaceCheckpointRepository;
import com.bikeescape.api.util.Validator;
import lombok.extern.java.Log;
import org.apache.commons.lang3.EnumUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Service
public class RaceCheckpointServiceImpl implements RaceCheckpointService {

    private final RaceCheckpointRepository raceCheckpointRepository;
    private final RaceService raceService;
    private final ModelMapper modelMapper;

    @Autowired
    public RaceCheckpointServiceImpl(RaceCheckpointRepository raceCheckpointRepository, RaceService raceService, ModelMapper modelMapper) {
        this.raceCheckpointRepository = raceCheckpointRepository;
        this.raceService = raceService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RaceCheckpoint> findAll() {
        return raceCheckpointRepository.findAll();
    }

    @Override
    public RaceCheckpoint findOne(String checkpointId) {
        return raceCheckpointRepository.findOne(checkpointId);
    }

    @Override
    public List<RaceCheckpoint> findByRaceId(String id) {
        return raceCheckpointRepository.findByRaceId(id);
    }

    @Override
    public RaceCheckpoint findByRaceIdAndNumber(String raceId, Long number) {
        return raceCheckpointRepository.findByRaceIdAndNumber(raceId, number);
    }

    @Override
    public void saveRaceCheckpoint(RaceCheckpointDTO raceCheckpointDTO) throws RaceValidationException {
        RaceCheckpoint raceCheckpoint = raceCheckpointRepository.findOne(raceCheckpointDTO.getId());
        raceCheckpoint.setName(raceCheckpointDTO.getName());
        raceCheckpoint.setDescription(raceCheckpointDTO.getDescription());
        raceCheckpoint.setQuestion(raceCheckpointDTO.getQuestion());
        raceCheckpoint.setAnswer(raceCheckpointDTO.getAnswer());
        raceCheckpoint.setAnswerType(AnswerType.valueOf(raceCheckpointDTO.getAnswerType().toUpperCase()));
        raceCheckpoint.setHint(raceCheckpointDTO.getHint());
        raceCheckpoint.setLatitude(raceCheckpointDTO.getLatitude());
        raceCheckpoint.setLongitude(raceCheckpointDTO.getLongitude());
        Validator.validateCheckpoint(raceCheckpoint);
        raceCheckpointRepository.save(raceCheckpoint);
        log.info("Race checkpoint saved: " + raceCheckpoint.toString());
    }

    @Override
    public RaceCheckpoint createRaceCheckpoint(RaceCheckpointDTO raceCheckpointDTO) throws RaceValidationException {
        RaceCheckpoint raceCheckpoint = convertRaceCheckpointToEntity(raceCheckpointDTO);
        Race race = raceService.findOne(raceCheckpointDTO.getRaceId());
        raceCheckpoint.setRace(race);
        if (!EnumUtils.isValidEnumIgnoreCase(AnswerType.class, raceCheckpointDTO.getAnswerType())) {
            throw new RaceValidationException("Answer type does not exist");
        }
        raceCheckpoint.setAnswerType(AnswerType.valueOf(raceCheckpointDTO.getAnswerType().toUpperCase()));
        raceCheckpoint.setNumber(race.getRaceCheckpoints().size() + 1L);
        raceCheckpointRepository.save(raceCheckpoint);
        race.setCheckpoints(raceCheckpoint.getNumber());
        Validator.validateCheckpoint(raceCheckpoint);
        raceCheckpointRepository.save(raceCheckpoint);
        raceService.save(race);
        log.info("Race checkpoint created: " + raceCheckpoint.toString());
        return raceCheckpoint;
    }

    @Override
    public void deleteRaceCheckpoint(String checkpointId) throws RaceValidationException {
        RaceCheckpoint raceCheckpoint = raceCheckpointRepository.findOne(checkpointId);
        String raceId = raceCheckpoint.getRace().getId();
        raceCheckpointRepository.delete(checkpointId);
        Race race = raceService.findOne(raceId);
        race.setCheckpoints((long) race.getRaceCheckpoints().size());
        raceService.save(race);
        log.info("Race checkpoint deleted: " + raceCheckpoint.toString());
    }

    @Override
    public void deleteAllCheckpointsByRaceId(String raceId) {
        if (raceCheckpointRepository.findByRaceId(raceId) != null) {
            raceCheckpointRepository.deleteByRaceId(raceId);
            log.info("All checkpoints for race: " + raceId + " deleted");
        }
    }

    private RaceCheckpoint convertRaceCheckpointToEntity(RaceCheckpointDTO raceCheckpointDTO) {
        return modelMapper.map(raceCheckpointDTO, RaceCheckpoint.class);
    }
}
