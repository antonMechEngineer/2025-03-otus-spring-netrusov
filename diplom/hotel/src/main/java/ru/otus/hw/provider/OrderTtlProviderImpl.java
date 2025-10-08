package ru.otus.hw.provider;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@SuppressWarnings("unused")
@Validated
@ConfigurationProperties(prefix = "business.ttl-minutes")
public record OrderTtlProviderImpl(
        @NotNull @Min(1L)
        Long notPaidOrder,
        @NotNull @Min(1L)
        Long requestedPaymentOrder
) implements OrderTtlProvider {}
