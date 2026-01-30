package com.dimidev.notificationservice.handler;

import com.dimidev.cm.core.event.EsteemDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "esteem-deleted-events-topic")
public class EsteemDeleteEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsteemDeleteEventHandler.class);

    @KafkaHandler
    public void handle(
            EsteemDeleteEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        LOGGER.info(
                "Получено событие удаления esteem из topic='{}', partition={} | likerId={} likedId={}",
                topic,
                partition,
                event.getLikerId(),
                event.getLikedId()
        );
    }
}