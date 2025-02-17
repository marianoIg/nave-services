package com.mariano.spacecrafts.api;

import com.mariano.spacecrafts.Fixture;
import com.mariano.spacecrafts.api.v1.SpacecraftController;
import com.mariano.spacecrafts.api.v1.mapper.SpacecraftMapper;
import com.mariano.spacecrafts.core.service.SpacecraftService;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftAlreadyExistsException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftInvalidDataException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftNotFoundException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftsError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpacecraftControllerTest {

    @Mock
    private SpacecraftMapper mapper = Mappers.getMapper(SpacecraftMapper.class);

    @Mock
    private SpacecraftService spacecraftService;

    @InjectMocks
    private SpacecraftController spacecraftController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(mapper);
        MockitoAnnotations.openMocks(spacecraftService);
        MockitoAnnotations.openMocks(spacecraftController);
    }

    @Test
    public void testGetAll() {
        Page<Spacecraft> spacecraftPage = new PageImpl<>(Collections.singletonList(Fixture.getSpacecraft()));

        when(spacecraftService.getAll(PageRequest.of(0, 5))).thenReturn(spacecraftPage);

        Page<Spacecraft> result = spacecraftService.getAll(PageRequest.of(0, 5));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Enterprise", result.getContent().getFirst().getName());
    }

    @Test
    public void testGetByName() {
        Page<Spacecraft> spacecraftPage = new PageImpl<>(Collections.singletonList(Fixture.getSpacecraft()));

        when(spacecraftService.getByName("Enterprise", PageRequest.of(0, 5))).thenReturn(spacecraftPage);

        Page<Spacecraft> result = spacecraftService.getByName("Enterprise", PageRequest.of(0, 5));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Enterprise", result.getContent().getFirst().getName());
    }

    @Test
    public void testGetById() {
        when(spacecraftService.getById(1L)).thenReturn(Fixture.getSpacecraft());

        Spacecraft result = spacecraftService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Enterprise", result.getName());
    }

    @Test
    public void testCreate() {
        when(spacecraftService.create(any(Spacecraft.class))).thenReturn(Fixture.getSpacecraft());

        Spacecraft result = spacecraftService.create(Fixture.getSpacecraft());

        assertNotNull(result);
        assertEquals("Enterprise", result.getName());
        verify(spacecraftService, times(1)).create(Fixture.getSpacecraft());
    }

    @Test
    public void testUpdate() {
        when(spacecraftService.update(any(Spacecraft.class))).thenReturn(Fixture.getSpacecraft());

        Spacecraft result = spacecraftService.update(Fixture.getSpacecraft());

        assertNotNull(result);
        assertEquals("Enterprise", result.getName());
        verify(spacecraftService, times(1)).update(Fixture.getSpacecraft());
    }

    @Test
    public void testDelete() {
        spacecraftService.delete(1L);
        verify(spacecraftService, times(1)).delete(1L);
    }

    @Test
    public void testGetById_NotFound() {
        when(spacecraftService.getById(1L)).thenThrow(new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_NEG_001,"1"));

        assertThrows(SpacecraftNotFoundException.class, () -> spacecraftService.getById(1L));

        verify(spacecraftService, times(1)).getById(1L);
    }

    @Test
    public void testCreate_AlreadyExists() {
        when(spacecraftService.create(any(Spacecraft.class))).thenThrow(new SpacecraftAlreadyExistsException(SpacecraftsError.NAV_ERR_NEG_005,"-2"));

        assertThrows(SpacecraftAlreadyExistsException.class, () -> spacecraftService.create(Fixture.getSpacecraft()));

        verify(spacecraftService, times(1)).create(any(Spacecraft.class));
    }

    @Test
    public void testCreate_InvalidData() {
        when(spacecraftService.create(any(Spacecraft.class))).thenThrow(new SpacecraftInvalidDataException(SpacecraftsError.NAV_ERR_NEG_005,"-2"));

        assertThrows(SpacecraftInvalidDataException.class, () -> spacecraftService.create(Fixture.getSpacecraft()));

        verify(spacecraftService, times(1)).create(any(Spacecraft.class));
    }

    @Test
    public void testUpdate_NotFound() {
        when(spacecraftService.update(any(Spacecraft.class))).thenThrow(new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_GEN_001,"2"));

        assertThrows(SpacecraftNotFoundException.class, () -> spacecraftService.update(Fixture.getSpacecraft_not_valid()));

        verify(spacecraftService, times(1)).update(any(Spacecraft.class));
    }

    @Test
    public void testDelete_NotFound() {
        doThrow(new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_GEN_001,"2")).when(spacecraftService).delete(1L);

        assertThrows(SpacecraftNotFoundException.class, () -> spacecraftService.delete(1L));

        verify(spacecraftService, times(1)).delete(1L);
    }

    @Test
    public void testMethodArgumentNotValidException() {
        when(spacecraftService.create(any(Spacecraft.class))).thenThrow(new SpacecraftInvalidDataException(SpacecraftsError.NAV_ERR_GEN_001,"2"));

        assertThrows(SpacecraftInvalidDataException.class, () -> spacecraftService.create(Fixture.getSpacecraft_not_valid()));

        verify(spacecraftService, times(1)).create(any(Spacecraft.class));
    }
}