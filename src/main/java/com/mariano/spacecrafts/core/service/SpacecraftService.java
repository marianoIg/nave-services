package com.mariano.spacecrafts.core.service;

import com.mariano.spacecrafts.core.common.CrudService;
import com.mariano.spacecrafts.core.usecase.CreateSpacecraftUseCase;
import com.mariano.spacecrafts.core.usecase.DeleteSpacecraftUseCase;
import com.mariano.spacecrafts.core.usecase.GetSpacecraftUseCase;
import com.mariano.spacecrafts.core.usecase.UpdateSpacecraftUseCase;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpacecraftService implements CrudService<Spacecraft> {

    private final CreateSpacecraftUseCase createSpacecraftUseCase;
    private final DeleteSpacecraftUseCase deleteSpacecraftUseCase;
    private final GetSpacecraftUseCase    getSpacecraftUseCase;
    private final UpdateSpacecraftUseCase updateSpacecraftUseCase;

    @Override
    public Page<Spacecraft> getAll(Pageable pageable) {
        return getSpacecraftUseCase.getAll(pageable);
    }

    @Override
    public Page<Spacecraft> getByName(String name, Pageable pageable) {
        return getSpacecraftUseCase.getByName(name, pageable);
    }

    @Override
    public Spacecraft getById(Long id) {
        return getSpacecraftUseCase.getSpacecraftById(id);
    }

    @Override
    public Spacecraft create(Spacecraft request) {
        return createSpacecraftUseCase.createSpacecraft(request);
    }

    @Override
    public Spacecraft update(Spacecraft request) {
        return updateSpacecraftUseCase.updateSpacecraft(request);
    }

    @Override
    public void delete(Long id) {
        deleteSpacecraftUseCase.deleteSpacecraft(id);
    }
}