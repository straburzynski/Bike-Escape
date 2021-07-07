package com.bikeescape.api.service;

import com.bikeescape.api.model.RaceType;
import com.bikeescape.api.repository.RaceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceTypeServiceImpl implements RaceTypeService {

    private final RaceTypeRepository raceTypeRepository;

    @Autowired
    public RaceTypeServiceImpl(RaceTypeRepository raceTypeRepository) {
        this.raceTypeRepository = raceTypeRepository;
    }

    @Override
    public List<RaceType> findAll() {
        return raceTypeRepository.findAll();
    }

    @Override
    public RaceType findOne(int id) {
        return raceTypeRepository.findOne(id);
    }

    @Override
    public RaceType findByName(String name) {
        return raceTypeRepository.findByName(name);
    }
}
