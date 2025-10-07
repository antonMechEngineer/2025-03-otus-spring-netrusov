package ru.otus.hw.provider;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("rentability")
public record RentabilityProviderImpl(
        @NotNull @Min(1)
        Integer period,
        @NotNull @Min(1L)
        Long threshold
) implements RentabilityProvider{}
