package com.mariano.spacecrafts.infraestructure;

import com.mariano.spacecrafts.core.domain.exceptions.Logging;
import com.mariano.spacecrafts.infrastructure.config.RabbitMQConfig;
import com.mariano.spacecrafts.infrastructure.messaging.RabbitConsumer;
import com.mariano.spacecrafts.infrastructure.messaging.RabbitProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@TestConfiguration
public class RabbitTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitProducer rabbitProducer;

    @Spy
    private RabbitConsumer rabbitConsumer;

    @Mock
    private Logging log; // Mockeamos el logger que usa RabbitConsumer

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRabbitMQSendMessage() {
        rabbitTemplate.send("logs_queue", null);
    }

    @Test
    public void testSendLog() {
        String message = "Test log message";

        rabbitProducer.sendLog(message);

        verify(rabbitTemplate).convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }

    @Test
    void testReceiveLog() {
        String message = "Test log message";

        rabbitConsumer.receiveLog(message);



    }
}
