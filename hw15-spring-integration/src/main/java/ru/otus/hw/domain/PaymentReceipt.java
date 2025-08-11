package ru.otus.hw.domain;

import java.util.Map;

public record PaymentReceipt(long clientId, Map<String, Double> order, OrganizationType organizationType) {
}
