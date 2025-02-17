package com.mariano.spacecrafts.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariano.spacecrafts.api.ApiAdvisor;
import com.mariano.spacecrafts.api.v1.mapper.SpacecraftMapper;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftAlreadyExistsException;
import com.mariano.spacecrafts.core.service.SpacecraftService;
import com.mariano.spacecrafts.core.domain.exceptions.Logging;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftInvalidDataException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftNotFoundException;
import com.mariano.spacecrafts.core.domain.exceptions.SpacecraftsError;
import com.mariano.spacecrafts.infrastructure.data.mapper.SpacecraftExternalMapper;
import com.mariano.spacecrafts.infrastructure.data.repository.SpacecraftRepository;
import com.mariano.spacecrafts.infrastructure.messaging.RabbitConsumer;
import com.mariano.spacecrafts.infrastructure.messaging.RabbitProducer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SqlGroup({
        @Sql(value = "classpath:/db/reset.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SpacecraftControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpacecraftService spacecraftService;

    @Autowired
    private SpacecraftMapper spacecraftMapper;

    @Autowired
    private SpacecraftExternalMapper SpacecraftExternalMapper;

    @Autowired
    private SpacecraftRepository spacecraftRepository;

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private RabbitConsumer RabbitConsumer;

    @InjectMocks
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Logging loggingService;

    @Autowired
    private ApiAdvisor ApiAdvisor;

    private static Long spacecraftId;

    @BeforeEach
    void setUp() throws Exception {
        String newSpacecraft = """
        {
            "name": "Galactica I",
            "series": "Galactica II",
            "craftType": "Battlestar",
            "crewCapacity": 2500,
            "weight": 1200.0
        }
        """;

        String response = mockMvc.perform(post("/api/spacecrafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newSpacecraft))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extrae el ID de la nave creada
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        spacecraftId = jsonNode.get("id").asLong();
    }

    @Test
    @SneakyThrows
    void getAllSpacecrafts() {
        mockMvc.perform(get("/api/spacecrafts")
                        .param("page", "1")
                        .param("size", "34")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(11));
    }

    @Test
    @SneakyThrows
    void getByName() {
        mockMvc.perform(get("/api/spacecrafts/search")
                        .param("name", "Galactica")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "name,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1));
    }

    @Test
    @SneakyThrows
    void getSpacecraftById() {
        mockMvc.perform(get("/api/spacecrafts/" + spacecraftId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Galactica I"))
                .andExpect(jsonPath("$.series").value("Galactica II"));
    }

    @Test
    @SneakyThrows
    void createSpacecraft() {
        String newSpacecraft = """
        {
            "name": "Galactica II",
            "series": "Galactica III",
            "craftType": "Battlestar",
            "crewCapacity": 2500,
            "weight": 1200.0
        }
        """;

        mockMvc.perform(post("/api/spacecrafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newSpacecraft))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Galactica II"))
                .andExpect(jsonPath("$.series").value("Galactica III"))
                .andExpect(jsonPath("$.craftType").value("Battlestar"))
                .andExpect(jsonPath("$.crewCapacity").value(2500))
                .andExpect(jsonPath("$.weight").value(1200.0));
    }


    @Test
    @SneakyThrows
    void updateSpacecraft() {
        String updatedSpacecraft = """
        {
            "name": "Updated Galactica",
            "series": "Galactica II",
            "craftType": "Battlestar",
            "crewCapacity": 2600,
            "weight": 1300.0
        }
        """;

        mockMvc.perform(put("/api/spacecrafts/" + spacecraftId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSpacecraft))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void deleteSpacecraft() {
        mockMvc.perform(delete("/api/spacecrafts/" + spacecraftId))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateSpacecraft_NotFound() {
        String updatedSpacecraft = "{\"name\":\"Updated Falcon\",\"series\":\"Star Wars\",\"craftType\":\"Freighter\",\"crewCapacity\":6,\"weight\":26000.0}";
        try {
            mockMvc.perform(put("/api/spacecrafts/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedSpacecraft))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("No se encontró nave espacial"));
            assertInstanceOf(SpacecraftNotFoundException.class, e.getCause());
        }
    }

    @Test
    void deleteSpacecraft_NotFound() {
        try {
            mockMvc.perform(delete("/api/spacecrafts/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("No se encontró nave espacial con id: 999"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("No se encontró nave espacial"));
            assertInstanceOf(SpacecraftNotFoundException.class, e.getCause());
        }
    }

    @Test
    void getSpacecraftById_NotFound() {
        try {
            mockMvc.perform(get("/api/spacecrafts/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("No se encontró nave espacial con id: 999"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("No se encontró nave espacial"));
            assertInstanceOf(SpacecraftNotFoundException.class, e.getCause());
        }
    }

    @Test
    void getSpacecraftById_Negative_Value() {
        try {
            mockMvc.perform(get("/api/spacecrafts/-1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("ID inválido, el ID de una nave debe ser mayor o igual a 1"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(SpacecraftsError.NAV_ERR_NEG_005.getMessage()));
            assertInstanceOf(SpacecraftInvalidDataException.class, e.getCause());
            if (e.getCause() instanceof SpacecraftInvalidDataException spacecraftException) {
                assertEquals(SpacecraftsError.NAV_ERR_NEG_005.getCode(), spacecraftException.getErrorType().getCode());
            }
        }
    }

    @Test

    void createSpacecraft_already_exist() {
        try {
            String newSpacecraft = """
            {
                "name": "Galactica I",
                "series": "Galactica II",
                "craftType": "Battlestar",
                "crewCapacity": 2500,
                "weight": 1200.0
            }
            """;

            mockMvc.perform(post("/api/spacecrafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newSpacecraft))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Galactica I"))
                    .andExpect(jsonPath("$.series").value("Galactica II"))
                    .andExpect(jsonPath("$.craftType").value("Battlestar"))
                    .andExpect(jsonPath("$.crewCapacity").value(2500))
                    .andExpect(jsonPath("$.weight").value(1200.0));
        } catch (Exception e) {
            assertInstanceOf(SpacecraftAlreadyExistsException.class, e.getCause());
        }
    }
}