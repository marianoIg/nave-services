package com.mariano.spacecrafts.core.usecase;

import com.mariano.spacecrafts.core.domain.Spacecraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetSpacecraftUseCase {

    Page<Spacecraft> getAll(Pageable pageRequest);

    Page<Spacecraft> getByName(String name, Pageable pageRequest);

    Spacecraft getSpacecraftById(Long id);
}
