package com.mariano.spacecrafts.infrastructure.data.mapper;

import com.mariano.spacecrafts.core.common.LayerMapper;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import com.mariano.spacecrafts.infrastructure.data.entity.SpacecraftEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpacecraftExternalMapper extends LayerMapper<Spacecraft, SpacecraftEntity> {
}
