package com.bikeescape.api.service;

import com.bikeescape.api.model.RaceType;

import java.util.List;

public interface RaceTypeService {

    List<RaceType> findAll();

    RaceType findOne(int id);

    RaceType findByName(String name);
}
