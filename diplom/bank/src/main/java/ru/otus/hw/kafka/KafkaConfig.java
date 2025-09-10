package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.otus.hw.kafka.dto.PaymentReq;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private static final String RESP_TOPIC_NAME = "payment-response";

    private static final String REQ_TOPIC_NAME = "payment-request";

    @Bean
    public ConsumerFactory<String, PaymentReq> kafkaConsumerFactory(
            KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, PaymentReq>>
    listenerContainerFactory(ConsumerFactory<String, PaymentReq> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentReq>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(false);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public NewTopic respTopic() {
        return TopicBuilder
                .name(RESP_TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic reqTopic() {
        return TopicBuilder
                .name(REQ_TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
