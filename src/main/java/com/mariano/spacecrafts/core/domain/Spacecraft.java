package com.mariano.spacecrafts.core.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Spacecraft {
    private Long id;
    private String name;
    private String series;
    private String craftType;
    private Integer crewCapacity;
    private Double weight;
}
