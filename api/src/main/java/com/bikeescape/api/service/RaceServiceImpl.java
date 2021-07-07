package com.bikeescape.api.service;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.model.Difficulty;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.RaceType;
import com.bikeescape.api.model.dto.RaceDTO;
import com.bikeescape.api.model.statistics.RaceStatistic;
import com.bikeescape.api.model.statistics.RaceValueStatistic;
import com.bikeescape.api.model.statistics.UserRaceStatistic;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.repository.RaceRepository;
import com.bikeescape.api.util.Validator;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class RaceServiceImpl implements RaceService {

    private final RaceRepository raceRepository;
    private final ModelMapper modelMapper;
    private final DifficultyService difficultyService;
    private final RaceTypeService raceTypeService;
    private final UserService userService;

    @Autowired
    public RaceServiceImpl(RaceRepository raceRepository, ModelMapper modelMapper, DifficultyService difficultyService, RaceTypeService raceTypeService, UserService userService) {
        this.raceRepository = raceRepository;
        this.modelMapper = modelMapper;
        this.difficultyService = difficultyService;
        this.raceTypeService = raceTypeService;
        this.userService = userService;
    }

    @Override
    public List<Race> findAll() {
        return raceRepository.findAll();
    }

    @Override
    public List<Race> findRacesByNameContaining(String name) {
        return raceRepository.findRacesByNameContaining(name);
    }

    @Override
    public List<Race> findRacesByAuthorId(Long authorId) {
        return raceRepository.findRacesByAuthorId(authorId);
    }

    @Override
    public Page<Race> findRacesByFilters(String name, String city, String difficulty, PageRequest pageRequest) {
        return raceRepository.findRacesByFilters(name, city, difficulty, pageRequest);
    }

    @Override
    public Race findOne(String id) {
        return raceRepository.findOne(id);
    }

    @Override
    public Page<Race> findAll(PageRequest pageRequest) {
        return raceRepository.findAll(pageRequest);
    }

    @Override
    public void saveRace(RaceDTO raceDTO) throws RaceValidationException {
        Race race = findOne(raceDTO.getId());
        Difficulty difficulty = difficultyService.findByName(raceDTO.getDifficulty());
        RaceType raceType = raceTypeService.findByName(raceDTO.getRaceType());
        race.setRaceType(raceType);
        race.setActive(raceDTO.getActive() != null ? raceDTO.getActive() : false);
        race.setCity(raceDTO.getCity());
        race.setDescription(raceDTO.getDescription());
        race.setDifficulty(difficulty);
        race.setEstimatedTime(raceDTO.getEstimatedTime());
        race.setName(raceDTO.getName());
        race.setPublic(raceDTO.getPublic() != null ? raceDTO.getPublic() : false);
        race.setSummary(raceDTO.getSummary());
        race.setFailDescription(raceDTO.getFailDescription());
        Validator.validateRace(race);
        this.raceRepository.save(race);
        log.info("Race saved: " + race.toString());
    }

    @Override
    public void save(Race race) throws RaceValidationException {
        Validator.validateRace(race);
        this.raceRepository.save(race);
        log.info("Race saved: " + race.toString());
    }

    @Override
    public Race createRace(RaceDTO raceDTO) throws Exception {
        Race race = convertRaceToEntity(raceDTO);
        Difficulty difficulty = difficultyService.findByName(raceDTO.getDifficulty());
        RaceType raceType = raceTypeService.findByName(raceDTO.getRaceType());
        User user = userService.findById(raceDTO.getAuthorId());
        race.setUser(user);
        race.setDifficulty(difficulty);
        race.setRaceType(raceType);
        race.setCreated(Calendar.getInstance().getTime());
        race.setCheckpoints(0L);
        Validator.validateRace(race);
        raceRepository.save(race);
        log.info("Race created: " + race.toString());
        return race;
    }

    @Override
    public void deleteRace(String raceId) {
        raceRepository.delete(raceId);
    }

    @Override
    public List<UserRaceStatistic> findTopUsersCreatedRaces(int limit) {
        return raceRepository.findTopUsersCreatedRaces()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRaceStatistic> findTopUsersRacesDone(int limit) {
        return raceRepository.findTopUsersRacesDone()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<RaceStatistic> findTopRacesDone(int limit) {
        return raceRepository.findTopRacesDone()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<RaceValueStatistic> findTopCityRaces(int limit) {
        return raceRepository.findTopCityRaces()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Long totalRacesCount() {
        return raceRepository.count();
    }

    @Override
    public Long totalRacesActive(boolean isActive) {
        return raceRepository.countByIsActive(isActive);
    }

    @Override
    public Long totalRacesPublic(boolean isPublic) {
        return raceRepository.countByIsPublic(isPublic);
    }

    private Race convertRaceToEntity(RaceDTO raceDTO) {
        return modelMapper.map(raceDTO, Race.class);
    }
}
