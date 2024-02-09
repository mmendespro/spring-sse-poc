package net.local.poc.sse.application.dto;

import java.util.UUID;

public record CustomerResponse(UUID customerId, String CustomerName, String customerEmail) {
}
