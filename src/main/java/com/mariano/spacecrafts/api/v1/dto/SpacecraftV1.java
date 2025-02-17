package com.mariano.spacecrafts.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos requeridos para crear o actualizar una nave espacial")
public class SpacecraftV1 {

    private Long id;
    @NotEmpty
    @Schema(description = "Nombre de la nave espacial", example = "Apollo 11")
    private String name;
    @NotEmpty
    @Schema(description = "Nombre de serie o pelicula", example = "Episodio 1")
    private String series;
    @NotEmpty
    @Schema(description = "Tipo de nave espacial", example = "Transbordador")
    private String craftType;
    @NotNull
    @Schema(description = "Capacidad de nave espacial", example = "30")
    private Integer crewCapacity;
    @NotNull
    @Schema(description = "Peso de la nave en toneladas", example = "3000")
    private Double weight;
}
