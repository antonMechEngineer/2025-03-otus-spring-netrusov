package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.domain.Advertisement;
import ru.otus.hw.domain.PathType;
import ru.otus.hw.mapper.AdvertisementMapper;
import ru.otus.hw.services.AdvertisementService;
import ru.otus.hw.services.FilterReceiptService;

@Slf4j
@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> paymentReceiptChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> advertisementChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> smsChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> pushChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow advertisementProviderFlow(AdvertisementService advertisementService,
                                                     FilterReceiptService filterReceiptService) {
        return IntegrationFlow.from(paymentReceiptChannel())
                .filter(filterReceiptService)
                .handle(advertisementService, "calculate")
                .<Advertisement, PathType>route(
                        Advertisement::pathType, mapping -> mapping
                                .channelMapping(PathType.SMS, "smsChannel")
                                .channelMapping(PathType.PUSH, "pushChannel"))
                .get();
    }

    @Bean
    public IntegrationFlow smsFlow(AdvertisementMapper advertisementMapper) {
        return IntegrationFlow.from(smsChannel())
                .transform(advertisementMapper, "toSms")
                .channel(advertisementChannel())
                .get();
    }

    @Bean
    public IntegrationFlow pushFlow(AdvertisementMapper advertisementMapper) {
        return IntegrationFlow.from(pushChannel())
                .transform(advertisementMapper, "toPush")
                .channel(advertisementChannel())
                .get();
    }
}
