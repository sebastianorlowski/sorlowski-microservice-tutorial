package com.sebastianorlowski.notification.rabbitmq;

import com.sebastianorlowski.clients.notification.NotificationRequest;
import com.sebastianorlowski.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public record NotificationConsumer(NotificationService notificationService) {

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificationRequest notificationRequest) {
        log.info("Consume {} from queue", notificationRequest);
        notificationService.send(notificationRequest);
    }
}
