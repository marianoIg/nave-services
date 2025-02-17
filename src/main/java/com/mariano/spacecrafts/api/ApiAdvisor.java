package com.mariano.spacecrafts.api;

import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftInvalidDataException;
import com.mariano.spacecrafts.core.domain.exceptions.Logging;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftAlreadyExistsException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftNotFoundException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftsError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ApiAdvisor {
    private static final String ERROR_CODE= "error_code";
    private static final String ERROR_MESSAGE= "error_message";
    private static final String ERROR_DETAIL= "error_details";
    private static final String ERROR_FIELD= "error_field";

    private final Logging loggingService;

    public ApiAdvisor(Logging loggingService) {
        this.loggingService = loggingService;
    }

    @ExceptionHandler(SpacecraftNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSpacecraftNotFound(SpacecraftNotFoundException ex) {
        logErrorMessages(ex);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_CODE, ex.getErrorType().getCode());
        response.put(ERROR_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(SpacecraftInvalidDataException.class)
    public ResponseEntity<Map<String, String>> handleInvalidData(SpacecraftInvalidDataException ex) {
        logErrorMessages(ex);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_CODE, ex.getErrorType().getCode());
        response.put(ERROR_MESSAGE, ex.getErrorType().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SpacecraftAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleSpacecraftAlreadyExists(SpacecraftAlreadyExistsException ex) {
        logErrorMessages(ex);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_CODE, ex.getErrorType().getCode());
        response.put(ERROR_MESSAGE, ex.getValue());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logErrorMessages(ex);
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put(ERROR_FIELD, error.getField());
            errorDetails.put(ERROR_MESSAGE, error.getDefaultMessage());
            errors.add(errorDetails);
        });

        response.put(ERROR_CODE, SpacecraftsError.NAV_ERR_NEG_003);
        response.put(ERROR_MESSAGE, SpacecraftsError.NAV_ERR_NEG_003.getMessage());
        response.put(ERROR_DETAIL, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logErrorMessages(ex);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_CODE, SpacecraftsError.NAV_ERR_GEN_001.name());
        response.put(ERROR_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleRequestParameterException(Exception ex) {
        logErrorMessages(ex);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_CODE, SpacecraftsError.NAV_ERR_NEG_003.name());
        response.put(ERROR_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logErrorMessages(ex);
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_CODE, SpacecraftsError.NAV_ERR_NEG_004.name());
        response.put(ERROR_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private void logErrorMessages(Exception ex) {
        loggingService.sendLog("ERROR: " + ex.getMessage());
    }
}