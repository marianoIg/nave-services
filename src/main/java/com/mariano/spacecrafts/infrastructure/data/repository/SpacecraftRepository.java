package com.mariano.spacecrafts.infrastructure.data.repository;

import com.mariano.spacecrafts.infrastructure.data.entity.SpacecraftEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpacecraftRepository extends JpaRepository <SpacecraftEntity,Long> {

    Page<SpacecraftEntity> findByNameContaining(String name, Pageable pageable);
    boolean existsByNameAndSeries(String name, String series);

}
