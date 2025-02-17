package com.mariano.spacecrafts.infraestructure.adapter;

import com.mariano.spacecrafts.Fixture;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftAlreadyExistsException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftNotFoundException;
import com.mariano.spacecrafts.infrastructure.adapters.SpacecraftAdapter;
import com.mariano.spacecrafts.infrastructure.data.entity.SpacecraftEntity;
import com.mariano.spacecrafts.infrastructure.data.mapper.SpacecraftExternalMapper;
import com.mariano.spacecrafts.infrastructure.data.repository.SpacecraftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpacecraftAdapterTest {

    @Mock
    private SpacecraftRepository spacecraftRepository;

    @Mock
    private SpacecraftExternalMapper mapper;

    @InjectMocks
    private SpacecraftAdapter spacecraftAdapter;

    private Spacecraft spacecraftDomain;
    private SpacecraftEntity spacecraftEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        spacecraftDomain = Fixture.getSpacecraft();
        spacecraftEntity = new SpacecraftEntity();
        spacecraftEntity.setId(1L);
        spacecraftEntity.setName(spacecraftDomain.getName());
        spacecraftEntity.setSeries(spacecraftDomain.getSeries());

        when(mapper.toOuterLayer(any(Spacecraft.class))).thenReturn(spacecraftEntity);
        when(mapper.toBusinessLayer(any(SpacecraftEntity.class))).thenReturn(spacecraftDomain);
    }

    void createSpacecraft_duplicate_throwsException() {
        when(spacecraftRepository.existsByNameAndSeries(spacecraftDomain.getName(), spacecraftDomain.getSeries()))
                .thenReturn(true);

        assertThrows(SpacecraftAlreadyExistsException.class, () -> spacecraftAdapter.createSpacecraft(spacecraftDomain));

        verify(spacecraftRepository, never()).save(any(SpacecraftEntity.class));
    }


    void getSpacecraftById_success() {
        when(spacecraftRepository.findById(1L)).thenReturn(Optional.of(spacecraftEntity));

        Spacecraft found = spacecraftAdapter.getSpacecraftById(1L);

        assertNotNull(found);
        assertEquals("Enterprise", found.getName());
        verify(spacecraftRepository, times(1)).findById(1L);
    }


    void getSpacecraftById_notFound_throwsException() {
        when(spacecraftRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(SpacecraftNotFoundException.class, () -> spacecraftAdapter.getSpacecraftById(999L));

        verify(spacecraftRepository, times(1)).findById(999L);
    }

    void updateSpacecraft_success() {
        when(spacecraftRepository.findById(1L)).thenReturn(Optional.of(spacecraftEntity));
        when(spacecraftRepository.save(any(SpacecraftEntity.class))).thenReturn(spacecraftEntity);

        Spacecraft updatedSpacecraft = new Spacecraft(1L, "Voyager", "NCC-74656", "Explorer", 350, 25000.0);
        Spacecraft updated = spacecraftAdapter.updateSpacecraft(updatedSpacecraft);

        assertEquals(350, updated.getCrewCapacity());
        verify(spacecraftRepository, times(1)).save(any(SpacecraftEntity.class));
    }


    void deleteSpacecraft_success() {
        when(spacecraftRepository.findById(1L)).thenReturn(Optional.of(spacecraftEntity));

        spacecraftAdapter.deleteSpacecraft(1L);

        verify(spacecraftRepository, times(1)).delete(spacecraftEntity);
    }


    void deleteSpacecraft_notFound_throwsException() {
        when(spacecraftRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(SpacecraftNotFoundException.class, () -> spacecraftAdapter.deleteSpacecraft(999L));

        verify(spacecraftRepository, times(1)).findById(999L);
    }
}
