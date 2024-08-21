package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.TreeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeProfileRepository extends JpaRepository<TreeProfile, Long> {
    // You can add custom query methods here if needed

    @Query("SELECT COUNT(DISTINCT t) FROM TreeProfile t JOIN t.seasons s")
    Long countSeasonalPlants();
}