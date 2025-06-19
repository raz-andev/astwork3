package Ast.homework.services;

import Ast.homework.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventProducer {
    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserEvent(String eventType, String email) {
        UserEvent event = new UserEvent(eventType, email);
        log.info("Publishing user event {}", event);
        kafkaTemplate.send(TOPIC, event);
        log.info("Отправка email-уведомления в Kafka: {}", event);
    }
}
