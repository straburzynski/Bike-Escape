package com.bikeescape.api.service;

import com.bikeescape.api.model.Difficulty;
import com.bikeescape.api.repository.DifficultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DifficultyServiceImpl implements DifficultyService {

    private final DifficultyRepository difficultyRepository;

    @Autowired
    public DifficultyServiceImpl(DifficultyRepository difficultyRepository) {
        this.difficultyRepository = difficultyRepository;
    }

    @Override
    public List<Difficulty> findAll() {
        return difficultyRepository.findAll();
    }

    @Override
    public Difficulty findByName(String name) {
        return difficultyRepository.findByName(name);
    }
}
