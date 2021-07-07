package com.bikeescape.api.repository;

import com.bikeescape.api.model.RaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceTypeRepository extends JpaRepository<RaceType, Integer> {

    RaceType findByName(String name);

}
