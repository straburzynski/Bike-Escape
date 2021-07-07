package com.bikeescape.api.service;

import com.bikeescape.api.model.Difficulty;

import java.util.List;

public interface DifficultyService {

    List<Difficulty> findAll();
    Difficulty findByName(String name);
}
