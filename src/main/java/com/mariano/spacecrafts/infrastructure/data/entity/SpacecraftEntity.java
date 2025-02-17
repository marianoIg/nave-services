package com.mariano.spacecrafts.infrastructure.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SPACECRAFTS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpacecraftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPACECRAFTS_SEQ")
    @SequenceGenerator(name = "SPACECRAFTS_SEQ", sequenceName = "SPACECRAFTS_SEQ", allocationSize = 1)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String series;

    @Column(name = "craft_type", nullable = false)
    private String craftType;

    @Column(name = "crew_capacity", nullable = false)
    private Integer crewCapacity;

    @Column(nullable = false)
    private Double weight;
}
