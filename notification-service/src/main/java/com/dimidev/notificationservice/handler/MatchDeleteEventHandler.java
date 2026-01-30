package com.dimidev.notificationservice.handler;

import com.dimidev.cm.core.event.MatchCreateDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "match-deleted-events-topic")
public class MatchDeleteEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchDeleteEventHandler.class);

    @KafkaHandler
    public void handle(
            MatchCreateDeleteEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        LOGGER.info(
                "Получено событие удаления матча из topic='{}', partition={} | matchId={} user1Id={} user2Id={}",
                topic,
                partition,
                event.getMatchId(),
                event.getUser1Id(),
                event.getUser2Id()
        );
    }
}

