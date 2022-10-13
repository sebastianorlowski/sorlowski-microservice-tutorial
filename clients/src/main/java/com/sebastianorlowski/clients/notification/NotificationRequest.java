package com.sebastianorlowski.clients.notification;

import java.time.LocalDateTime;

public record NotificationRequest(String message,
                                  String toCustomerEmail,
                                  Long toCustomerId) {
}
