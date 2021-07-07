package com.bikeescape.api.repository;

import com.bikeescape.api.model.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DifficultyRepository extends JpaRepository<Difficulty, Integer> {

    List<Difficulty> findAll();
    Difficulty findByName(String name);
}