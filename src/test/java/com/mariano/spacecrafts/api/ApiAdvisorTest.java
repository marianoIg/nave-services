package com.mariano.spacecrafts.api;

import com.mariano.spacecrafts.core.domain.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiAdvisorTest {

    @Mock
    private Logging loggingService;

    @InjectMocks
    private ApiAdvisor apiAdvisor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleSpacecraftNotFound() {
        String error= "No se encontró nave espacial con id: 1";
        SpacecraftNotFoundException exception = new SpacecraftNotFoundException(SpacecraftsError.NAV_ERR_NEG_001, "1");
        ResponseEntity<Map<String, String>> response = apiAdvisor.handleSpacecraftNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(SpacecraftsError.NAV_ERR_NEG_001.getCode(), response.getBody().get("error_code"));
        assertEquals(error, response.getBody().get("error_message"));
        verify(loggingService).sendLog("ERROR: "+ error);
    }

    @Test
    void testHandleInvalidData() {
        SpacecraftInvalidDataException exception = new SpacecraftInvalidDataException(SpacecraftsError.NAV_ERR_NEG_005,"-1");
        ResponseEntity<Map<String, String>> response = apiAdvisor.handleInvalidData(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(SpacecraftsError.NAV_ERR_NEG_005.getCode(), response.getBody().get("error_code"));
        assertEquals(SpacecraftsError.NAV_ERR_NEG_005.getMessage(), response.getBody().get("error_message"));
        verify(loggingService).sendLog("ERROR: " + exception.getMessage());
    }

    @Test
    void testHandleSpacecraftAlreadyExists() {
        SpacecraftAlreadyExistsException exception = new SpacecraftAlreadyExistsException(SpacecraftsError.NAV_ERR_NEG_005, "Spacecraft exists");
        ResponseEntity<Map<String, String>> response = apiAdvisor.handleSpacecraftAlreadyExists(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(SpacecraftsError.NAV_ERR_NEG_005.getCode(), response.getBody().get("error_code"));
        assertEquals("Spacecraft exists", response.getBody().get("error_message"));
        verify(loggingService).sendLog("ERROR: Spacecraft exists");
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<Map<String, String>> response = apiAdvisor.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(SpacecraftsError.NAV_ERR_GEN_001.name(), response.getBody().get("error_code"));
        assertEquals("Unexpected error", response.getBody().get("error_message"));
        verify(loggingService).sendLog("ERROR: Unexpected error");
    }

    @Test
    void testHandleRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");
        ResponseEntity<Map<String, String>> response = apiAdvisor.handleRequestParameterException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(SpacecraftsError.NAV_ERR_NEG_003.name(), response.getBody().get("error_code"));
        assertEquals(exception.getMessage(), response.getBody().get("error_message"));
        verify(loggingService).sendLog("ERROR: " + exception.getMessage());
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Integrity error");
        ResponseEntity<Map<String, String>> response = apiAdvisor.handleDataIntegrityViolationException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(SpacecraftsError.NAV_ERR_NEG_004.name(), response.getBody().get("error_code"));
        assertEquals("Integrity error", response.getBody().get("error_message"));
    }
}