package org.example.coursemanagementsystem.dto.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedProducer {

    @Value("${kafka.topics.user-created}")//yml-dan goturur
    private String userCreatedTopic;

    @Value("${kafka.topics.forgot-password}")
    private String forgotPasswordTopic;

    private final KafkaTemplate<String, UserCreatedEvent> userCreatedTemplate;
    private final KafkaTemplate<String, ForgotPasswordEvent> forgotPasswordTemplate;

    public void sendUserCreatedEvent(UserCreatedEvent event) {
        userCreatedTemplate.send(userCreatedTopic, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("UserCreatedEvent göndərilmədi — userId: {}, xəta: {}",
                                event.getUserId(), ex.getMessage());
                    } else {
                        log.info("UserCreatedEvent göndərildi — topic: {}, userId: {}",
                                userCreatedTopic, event.getUserId());
                    }
                });
    }

    public void sendForgotPasswordEvent(ForgotPasswordEvent event) {
        forgotPasswordTemplate.send(forgotPasswordTopic, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("ForgotPasswordEvent göndərilmədi — userId: {}, xəta: {}",
                                event.getUserId(), ex.getMessage());
                    } else {
                        log.info("ForgotPasswordEvent göndərildi — userId: {}", event.getUserId());
                    }
                });
    }
}
