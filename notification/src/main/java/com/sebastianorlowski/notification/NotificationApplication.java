package com.sebastianorlowski.notification;

import com.sebastianorlowski.amqp.RabbitMQMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
        "com.sebastianorlowski.amqp",
        "com.sebastianorlowski.notification"
})
@EnableEurekaClient
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer rabbitMQMessageProducer,
                                        NotificationConfig notificationConfig) {
        return args -> {
            rabbitMQMessageProducer.publish(
                    new Person("Sebastian", "Orlowski", 27),
                    notificationConfig.getInternalExchange(),
                    notificationConfig.getNotificationRoutingKey());
        };
    }

    record Person(String firstName, String lastName, int age) {
    }
}
