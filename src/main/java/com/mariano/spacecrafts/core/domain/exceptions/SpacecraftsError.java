package com.mariano.spacecrafts.core.domain.exceptions;

public enum SpacecraftsError implements SpacecraftsErrorType {

    NAV_ERR_NEG_001("NAV_ERR_NEG_001", "No se encontró nave espacial con id: %s"),
    NAV_ERR_NEG_002("NAV_ERR_NEG_002", "Ya existe una nave con nombre: %s y serie: %s"),
    NAV_ERR_NEG_003("NAV_ERR_NEG_003", "Campo/s inválido/s"),
    NAV_ERR_NEG_004("NAV_ERR_NEG_004", "%s"),
    NAV_ERR_NEG_005("NAV_ERR_NEG_005", "ID inválido, el ID de una nave debe ser mayor o igual a 1."),

    NAV_ERR_GEN_001("NAV_ERR_GEN_001", "Excepción no controlada, intente nuevamente"),

    NAV_ERR_SEC_001("NAV_ERR_SEC_001", "Esta acción requiere ingresar un usuario y contraseña válidos"),
    NAV_ERR_SEC_002("NAV_ERR_SEC_002", "Esta acción debe realizarse con un usuario y contraseña autorizados");

    private final String code;
    private final String description;

    SpacecraftsError(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}