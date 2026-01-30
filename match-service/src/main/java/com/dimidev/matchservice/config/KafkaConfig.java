package com.dimidev.matchservice.config;

import com.dimidev.cm.core.event.EsteemCreateUpdateEvent;
import com.dimidev.cm.core.event.MatchCreateDeleteEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private int deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private int linger;

    @Value("${spring.kafka.producer.properties.request.timeout.request.ms}")
    private int requestTimeout;

    Map<String, Object> producerConfigs() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        config.put(ProducerConfig.ACKS_CONFIG, acks);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        config.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);

        return config;
    }

    @Bean
    ProducerFactory<String, EsteemCreateUpdateEvent> esteemCreateUpdateProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    ProducerFactory<String, Object> objectProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    ProducerFactory<String, MatchCreateDeleteEvent> matchCreateUpdateProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean("esteemCreateupdateEventsKafkaTemplate")
    KafkaTemplate<String, EsteemCreateUpdateEvent> esteemCreateupdateEventsKafkaTemplate(){
        return new KafkaTemplate<>(esteemCreateUpdateProducerFactory());
    }

    @Bean("kafkaTemplate")
    KafkaTemplate<String, Object> kafkaTemplate(){
        return new KafkaTemplate<>(objectProducerFactory());
    }

    @Bean("matchCreateupdateEventsKafkaTemplate")
    KafkaTemplate<String, MatchCreateDeleteEvent> matchCreateupdateEventsKafkaTemplate(){
        return new KafkaTemplate<>(matchCreateUpdateProducerFactory());
    }

    // Kafka Topics

    @Bean
    NewTopic createEsteemCreatedEventsTopic() {
        return TopicBuilder.name("esteem-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createEsteemUpdateEventsTopic() {
        return TopicBuilder.name("esteem-updated-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createEsteemDeleteEventsTopic() {
        return TopicBuilder.name("esteem-deleted-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createMatchCreatedEventsTopic() {
        return TopicBuilder.name("match-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }

    @Bean
    NewTopic createMatchDeletedEventsTopic() {
        return TopicBuilder.name("match-deleted-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
