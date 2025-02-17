package com.mariano.spacecrafts.api.v1.mapper;

import com.mariano.spacecrafts.api.v1.dto.SpacecraftV1;
import com.mariano.spacecrafts.core.common.LayerMapper;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpacecraftMapper extends LayerMapper<Spacecraft, SpacecraftV1> {

}