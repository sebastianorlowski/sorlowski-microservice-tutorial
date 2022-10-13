package com.sebastianorlowski.customer;

import com.sebastianorlowski.amqp.RabbitMQMessageProducer;
import com.sebastianorlowski.clients.fraud.FraudCheckResponse;
import com.sebastianorlowski.clients.fraud.FraudClient;
import com.sebastianorlowski.clients.notification.NotificationClient;
import com.sebastianorlowski.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository,
                              RestTemplate restTemplate,
                              FraudClient fraudClient,
                              RabbitMQMessageProducer producer) {

    private static final String EXCHANGE = "internal.exchange";
    private static final String ROUTING_KEY = "internal.notification.routing-key";

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new RuntimeException("It's fraud");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                String.format("Hello %s, welcome to microservice.", customer.getFirstName()),
                customer.getEmail(),
                customer.getId());

        producer.publish(notificationRequest, EXCHANGE, ROUTING_KEY);
    }
}
