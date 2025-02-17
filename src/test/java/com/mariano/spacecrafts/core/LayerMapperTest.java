package com.mariano.spacecrafts.core;

import com.mariano.spacecrafts.core.common.LayerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LayerMapperTest {

    private LayerMapper<String, Integer> layerMapper;

    @BeforeEach
    void setUp() {
        // Mock de LayerMapper para simular conversión entre Integer y String
        layerMapper = Mockito.mock(LayerMapper.class);

        when(layerMapper.toBusinessLayer(1)).thenReturn("1");
        when(layerMapper.toOuterLayer("1")).thenReturn(1);

        when(layerMapper.toBusinessLayer(2)).thenReturn("2");
        when(layerMapper.toOuterLayer("2")).thenReturn(2);
    }

    @Test
    void testToBusinessLayer() {
        assertEquals("1", layerMapper.toBusinessLayer(1));
        assertEquals("2", layerMapper.toBusinessLayer(2));
    }

    @Test
    void testToOuterLayer() {
        assertEquals(1, layerMapper.toOuterLayer("1"));
        assertEquals(2, layerMapper.toOuterLayer("2"));
    }

    @Test
    void testToOuterLayer_List() {
        List<String> businessList = List.of("1", "2");
        List<Integer> outerList = List.of(1, 2);

        when(layerMapper.toOuterLayer(businessList)).thenCallRealMethod();
        when(layerMapper.toOuterLayer("1")).thenReturn(1);
        when(layerMapper.toOuterLayer("2")).thenReturn(2);

        assertEquals(outerList, layerMapper.toOuterLayer(businessList));
    }

    @Test
    void testToBusinessLayer_List() {
        List<Integer> outerList = List.of(1, 2);
        List<String> businessList = List.of("1", "2");

        when(layerMapper.toBusinessLayer(outerList)).thenCallRealMethod();
        when(layerMapper.toBusinessLayer(1)).thenReturn("1");
        when(layerMapper.toBusinessLayer(2)).thenReturn("2");

        assertEquals(businessList, layerMapper.toBusinessLayer(outerList));
    }

    @Test
    void testToOuterLayer_Page() {
        Page<String> businessPage = new PageImpl<>(List.of("1", "2"), PageRequest.of(0, 2), 2);
        Page<Integer> expectedOuterPage = new PageImpl<>(List.of(1, 2), PageRequest.of(0, 2), 2);

        when(layerMapper.toOuterLayer(businessPage)).thenCallRealMethod();
        when(layerMapper.toOuterLayer("1")).thenReturn(1);
        when(layerMapper.toOuterLayer("2")).thenReturn(2);

        Page<Integer> result = layerMapper.toOuterLayer(businessPage);
        assertEquals(expectedOuterPage.getContent(), result.getContent());
    }

    @Test
    void testToBusinessLayer_Page() {
        Page<Integer> outerPage = new PageImpl<>(List.of(1, 2), PageRequest.of(0, 2), 2);
        Page<String> expectedBusinessPage = new PageImpl<>(List.of("1", "2"), PageRequest.of(0, 2), 2);

        when(layerMapper.toBusinessLayer(outerPage)).thenCallRealMethod();
        when(layerMapper.toBusinessLayer(1)).thenReturn("1");
        when(layerMapper.toBusinessLayer(2)).thenReturn("2");

        Page<String> result = layerMapper.toBusinessLayer(outerPage);
        assertEquals(expectedBusinessPage.getContent(), result.getContent());
    }

    @Test
    void testToBusinessLayer_null() {
        assertNull(layerMapper.toBusinessLayer((Integer) null), "Debe retornar null cuando la entrada es null");
    }

    @Test
    void testToOuterLayer_null() {
        assertTrue(layerMapper.toOuterLayer((List<String>) null).isEmpty(), "Debe retornar una lista vacía cuando la entrada es null");    }
}

