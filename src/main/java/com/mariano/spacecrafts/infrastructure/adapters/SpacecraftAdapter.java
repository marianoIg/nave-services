package com.mariano.spacecrafts.infrastructure.adapters;

import com.mariano.spacecrafts.core.usecase.CreateSpacecraftUseCase;
import com.mariano.spacecrafts.core.usecase.DeleteSpacecraftUseCase;
import com.mariano.spacecrafts.core.usecase.GetSpacecraftUseCase;
import com.mariano.spacecrafts.core.usecase.UpdateSpacecraftUseCase;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftAlreadyExistsException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftNotFoundException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftsError;
import com.mariano.spacecrafts.infrastructure.data.mapper.SpacecraftExternalMapper;
import com.mariano.spacecrafts.infrastructure.data.repository.SpacecraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpacecraftAdapter implements GetSpacecraftUseCase, CreateSpacecraftUseCase, UpdateSpacecraftUseCase, DeleteSpacecraftUseCase {

    private final SpacecraftRepository spacecraftRepository;
    private final SpacecraftExternalMapper mapper;

    @Override
    public Spacecraft createSpacecraft(Spacecraft spacecraft) {
        if (spacecraftRepository.existsByNameAndSeries(spacecraft.getName(), spacecraft.getSeries())) {
            String errorMessage = "Ya existe una nave con nombre: " + spacecraft.getName() + " y serie: " + spacecraft.getSeries();
            throw new SpacecraftAlreadyExistsException(SpacecraftsError.NAV_ERR_NEG_002,errorMessage
            );
        }
        var spacecraftEntity = mapper.toOuterLayer(spacecraft);
        return mapper.toBusinessLayer(spacecraftRepository.saveAndFlush(spacecraftEntity));
    }

    @Override
    public void deleteSpacecraft(Long id) {
        if (!spacecraftRepository.existsById(id)) {
            throw new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_NEG_001, id.toString());
        }
        spacecraftRepository.deleteById(id);
    }

    @Override
    public Page<Spacecraft> getAll(Pageable pageRequest) {
        return mapper.toBusinessLayer(spacecraftRepository.findAll(pageRequest));
    }

    @Override
    @Cacheable(value = "spacecraftsByName", key = "#name")
    public Page<Spacecraft> getByName(String name, Pageable pageRequest) {
        return mapper.toBusinessLayer(spacecraftRepository.findByNameContaining(name,pageRequest));
    }

    @Override
    @Cacheable(value = "spacecraftById", key = "#id")
    public Spacecraft getSpacecraftById(Long id) {
        return spacecraftRepository.findById(id)
                .map(mapper::toBusinessLayer)
                .orElseThrow(() -> new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_NEG_001, id.toString()));    }

    @Override
    public Spacecraft updateSpacecraft(Spacecraft spacecraft) {
        var existingSpacecraft = spacecraftRepository.findById(spacecraft.getId());
        if (existingSpacecraft.isEmpty()) {
            throw new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_NEG_001, spacecraft.getId().toString());
        }

        var spacecraftEntity = mapper.toOuterLayer(spacecraft);
        return mapper.toBusinessLayer(spacecraftRepository.saveAndFlush(spacecraftEntity));
    }
}