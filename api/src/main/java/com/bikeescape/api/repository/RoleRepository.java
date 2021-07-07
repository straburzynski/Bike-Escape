package com.bikeescape.api.repository;

import com.bikeescape.api.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Authority, Integer> {
    Authority findByName(String name);

}
