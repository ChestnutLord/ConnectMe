package com.dimidev.matchservice.service;

import com.dimidev.cm.core.event.MatchCreateDeleteEvent;
import com.dimidev.matchservice.exception.NotFoundException;
import com.dimidev.matchservice.model.Match;
import com.dimidev.matchservice.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MatchService {

    private final MatchRepository rep;
    private final KafkaTemplate<String, MatchCreateDeleteEvent> kafkaTemplate;
    private final static Logger LOGGER = LoggerFactory.getLogger(MatchService.class);

    public MatchService(MatchRepository rep,
                        @Qualifier("matchCreateupdateEventsKafkaTemplate") KafkaTemplate<String, MatchCreateDeleteEvent> kafkaTemplate) {
        this.rep = rep;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Match> getAllByUserId(Long userId) {
        return rep.findByUser1IdOrUser2Id(userId, userId);
    }

    public Match getByIdAndUserId(Long id, Long userId) {
        return rep.findByIdAndUser1IdOrIdAndUser2Id(id, userId, id, userId)
                .orElseThrow(() -> new NotFoundException(id, Match.class));
    }

    public Match create(Match match) {
        Match save = rep.save(match);
        Long matchId = match.getId();
        MatchCreateDeleteEvent matchCreateEvent = new MatchCreateDeleteEvent(matchId, match.getUser1Id(), match.getUser2Id());

        CompletableFuture<SendResult<String, MatchCreateDeleteEvent>> future = kafkaTemplate
                .send("match-created-events-topic", matchId.toString(), matchCreateEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        return save;
    }

    public void sortAndCreate(Long user1Id, Long user2Id) {
        long firstId = Math.min(user1Id, user2Id);
        long secondId = Math.max(user1Id, user2Id);

        Match match = new Match();
        match.setUser1Id(firstId);
        match.setUser2Id(secondId);

        create(match);
    }

    public void deleteByUserIds(Long user1Id, Long user2Id) {
        long first = Math.min(user1Id, user2Id);
        long second = Math.max(user1Id, user2Id);

        Match match = rep.findByUser1IdAndUser2Id(first, second)
                .orElseThrow(() -> new NotFoundException(first + "," + second, Match.class));
        rep.delete(match);

        Long matchId = match.getId();

        // Kafka

        MatchCreateDeleteEvent matchDeleteEvent = new MatchCreateDeleteEvent(matchId, match.getUser1Id(), match.getUser2Id());

        CompletableFuture<SendResult<String, MatchCreateDeleteEvent>> future = kafkaTemplate
                .send("match-deleted-events-topic", matchId.toString(), matchDeleteEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });
    }
}
