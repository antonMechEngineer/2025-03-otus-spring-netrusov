package ru.otus.hw.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ttl")
public class OrderTtlProviderImpl implements OrderTtlProvider{

    private Long notPaidOrder;

    private Long requestedPaymentOrder;
}
