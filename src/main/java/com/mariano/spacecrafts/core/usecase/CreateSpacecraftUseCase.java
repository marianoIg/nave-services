package com.mariano.spacecrafts.core.usecase;

import com.mariano.spacecrafts.core.domain.Spacecraft;

public interface CreateSpacecraftUseCase {
    Spacecraft createSpacecraft(Spacecraft spacecraft);
}
