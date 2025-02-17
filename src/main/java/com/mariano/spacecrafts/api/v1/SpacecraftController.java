package com.mariano.spacecrafts.api.v1;

import com.mariano.spacecrafts.api.common.CrudController;
import com.mariano.spacecrafts.api.v1.mapper.SpacecraftMapper;
import com.mariano.spacecrafts.api.v1.dto.SpacecraftV1;
import com.mariano.spacecrafts.core.service.SpacecraftService;
import com.mariano.spacecrafts.core.domain.Spacecraft;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Validated
@RequestMapping("/api/spacecrafts")
@Tag(name = "Spacecrafts", description = "CRUD de naves espaciales")
public class SpacecraftController extends CrudController<SpacecraftV1, Spacecraft> {

    private final SpacecraftMapper mapper;
    private final SpacecraftService spacecraftService;

    public SpacecraftController(SpacecraftMapper mapper, SpacecraftService spacecraftService) {
        super(mapper,spacecraftService);
        this.mapper = mapper;
        this.spacecraftService = spacecraftService;
    }

    @Operation(
            summary = "Obtener todas las naves espaciales",
            description = "Retorna una lista de todas las naves espaciales registradas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
            }
    )
    @GetMapping
    public ResponseEntity<Page<SpacecraftV1>> getAll(
            @PageableDefault(size = 5, page = 0) Pageable pageable) {return super.getAll(pageable);
    }

    @Operation(
            summary = "Obtener naves espaciales por nombre",
            description = "Retorna una lista de naves espaciales que coinciden con el nombre proporcionado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<Page<SpacecraftV1>> getByName(
            @Parameter(description = "Nombre de la nave espacial", example = "Enterprise")
            @RequestParam String name,
            @PageableDefault(size = 5, page = 0) Pageable pageable) {
        return super.getByName(name, pageable);
    }

    @Operation(
            summary = "Obtener una nave espacial por ID",
            description = "Retorna los detalles de una nave espacial específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Nave encontrada",
                            content = @Content(schema = @Schema(implementation = SpacecraftV1.class))),
                    @ApiResponse(responseCode = "404", description = "Nave no encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<SpacecraftV1> getById(
            @Parameter(description = "ID de la nave espacial", example = "1") @PathVariable Long id) {
       return super.getById(id);
    }

    @Operation(
            summary = "Registrar una nueva nave espacial",
            description = "Crea y guarda una nueva nave espacial en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Nave creada exitosamente",
                            content = @Content(schema = @Schema(implementation = SpacecraftV1.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<SpacecraftV1> create(@RequestBody @Valid SpacecraftV1 spacecraft) {
        ResponseEntity<SpacecraftV1> response = super.create(spacecraft);
        URI location = URI.create("/api/spacecrafts/" + response.getBody().getId());
        return ResponseEntity.created(location).body(response.getBody());
    }

    @Operation(
            summary = "Actualizar una nave espacial",
            description = "Modifica los datos de una nave espacial existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Nave actualizada correctamente",
                            content = @Content(schema = @Schema(implementation = SpacecraftV1.class))),
                    @ApiResponse(responseCode = "404", description = "Nave no encontrada"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SpacecraftV1> update(
            @Parameter(description = "ID de la nave espacial", example = "1") @PathVariable Long id,
            @RequestBody @Valid SpacecraftV1 spacecraft) {
        spacecraft.setId(id);
        return super.update( spacecraft);
    }

    @Operation(
            summary = "Eliminar una nave espacial",
            description = "Elimina una nave espacial por su ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Nave eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Nave no encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la nave espacial", example = "1") @PathVariable Long id) {
        return super.delete(id);
    }
}
