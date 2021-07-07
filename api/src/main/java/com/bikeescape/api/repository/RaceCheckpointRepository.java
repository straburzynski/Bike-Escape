package com.bikeescape.api.repository;

import com.bikeescape.api.model.RaceCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RaceCheckpointRepository extends JpaRepository<RaceCheckpoint, String> {

    List<RaceCheckpoint> findByRaceId(String id);
    RaceCheckpoint findByRaceIdAndNumber(String raceId, Long number);
    RaceCheckpoint findOne(String checkpointId);

    @Transactional
    List<RaceCheckpoint> deleteByRaceId(String raceId);
}
