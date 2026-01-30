package com.dimidev.notificationservice.handler;

import com.dimidev.cm.core.event.EsteemCreateUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(
        topics = {
                "esteem-created-events-topic",
                "esteem-updated-events-topic"
        }
)
public class EsteemCreateUpdateEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsteemCreateUpdateEventHandler.class);

    @KafkaHandler
    public void handle(
            EsteemCreateUpdateEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        LOGGER.info(
                "Получено событие esteem из topic='{}', partition={} | likerId={} likedId={} esteem={}",
                topic,
                partition,
                event.getLikerId(),
                event.getLikedId(),
                event.isEsteem());
    }
}
