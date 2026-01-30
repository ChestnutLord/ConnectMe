package com.dimidev.matchservice.service;

import com.dimidev.cm.core.event.EsteemCreateUpdateEvent;
import com.dimidev.cm.core.event.EsteemDeleteEvent;
import com.dimidev.matchservice.client.UserClient;
import com.dimidev.matchservice.exception.NotFoundException;
import com.dimidev.matchservice.model.Esteem;
import com.dimidev.matchservice.repository.EsteemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EsteemService {

    private final EsteemRepository rep;
    private final MatchService matchService;
    private final UserClient userClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, EsteemCreateUpdateEvent> esteemCreateupdateEventsKafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(EsteemService.class);

    public EsteemService(EsteemRepository rep,
                         MatchService matchService,
                         UserClient userClient,
                         @Qualifier("kafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate,
                         @Qualifier("esteemCreateupdateEventsKafkaTemplate") KafkaTemplate<String, EsteemCreateUpdateEvent> esteemCreateupdateEventsKafkaTemplate) {
        this.rep = rep;
        this.matchService = matchService;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
        this.esteemCreateupdateEventsKafkaTemplate = esteemCreateupdateEventsKafkaTemplate;
    }

    public List<Esteem> getAllByUserId(Long userId) {
        return rep.findByLikerId(userId);
    }

    public Esteem getByIdAndUserId(Long likeId, Long userId) {
        return rep.findByIdAndLikerId(likeId, userId)
                .orElseThrow(() -> new NotFoundException(likeId, Esteem.class));
    }

    // When creating a positive esteem, check if there is a mutual positive esteem, and if so, create a match.
    public Esteem create(Long likerId, Long likedId, boolean esteemValue) {
        if (likerId.equals(likedId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot evaluate yourself."
            );
        }
        if (rep.findByLikerId(likerId).stream()
                .anyMatch(e -> e.getLikedId().equals(likedId))) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An esteem between these users already exists. Use the update method to modify it."
            );
        }

        // Validate that both users exist
        userClient.getById(likerId);
        userClient.getById(likedId);

        Esteem newEsteem = new Esteem();
        newEsteem.setLikerId(likerId);
        newEsteem.setLikedId(likedId);
        newEsteem.setEsteem(esteemValue);

        rep.save(newEsteem);

        // Kafka

        EsteemCreateUpdateEvent esteemCreatedEvent = new EsteemCreateUpdateEvent(likerId, likedId, esteemValue);

        CompletableFuture<SendResult<String, EsteemCreateUpdateEvent>> future = esteemCreateupdateEventsKafkaTemplate
                .send("esteem-created-events-topic", newEsteem.getId().toString(), esteemCreatedEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        // If the esteem is positive, check for mutuality
        if (esteemValue) {
            if (rep.findByLikerId(likedId).stream()
                    .anyMatch(e -> e.getLikedId().equals(likerId) && e.isEsteem())) {
                matchService.sortAndCreate(likerId, likedId);
            }
        }

        return newEsteem;
    }

    // On positive update, verify mutual esteem and create a match.
    // On negative update, check for existing match and remove it.
    public Esteem update(Long id, Long userId, boolean newEsteem) {

        Esteem existing = getByIdAndUserId(id, userId);
        Long likedId = existing.getLikedId();

        if (!existing.getLikerId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update someone else's esteem."
            );
        }

        existing.setEsteem(newEsteem);
        rep.save(existing);

        // Kafka

        EsteemCreateUpdateEvent esteemUpdateEvent = new EsteemCreateUpdateEvent(userId, likedId, newEsteem);

        CompletableFuture<SendResult<String, EsteemCreateUpdateEvent>> future = esteemCreateupdateEventsKafkaTemplate
                .send("esteem-updated-events-topic", id.toString(), esteemUpdateEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        if (newEsteem) {
            if (rep.findByLikerId(likedId).stream()
                    .anyMatch(e -> e.getLikedId().equals(userId) && e.isEsteem())) {
                matchService.sortAndCreate(userId, likedId);
            }
        } else {
            if (rep.findByLikerId(likedId).stream()
                    .anyMatch(e -> e.getLikedId().equals(userId) && e.isEsteem())) {
                matchService.deleteByUserIds(userId, likedId);
            }
        }

        return existing;
    }


    // Deleting a positive esteem triggers match removal if one exists.
    public void deleteByIdAndUserId(Long id, Long userId) {
        rep.findByIdAndLikerId(id, userId).orElseThrow(() -> new NotFoundException(id, Esteem.class));
        Esteem esteem = getByIdAndUserId(id, userId);
        Long likedId = esteem.getLikedId();

        if (esteem.isEsteem()) {
            if (rep.findByLikerId(likedId).stream()
                    .anyMatch(e -> e.getLikedId().equals(userId) && e.isEsteem())) {
                matchService.deleteByUserIds(userId, likedId);
            }
        }

        rep.deleteById(id);

        // Kafka

        EsteemDeleteEvent esteemDeleteEvent = new EsteemDeleteEvent(userId, likedId);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate
                .send("esteem-deleted-events-topic", id.toString(), esteemDeleteEvent);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });
    }
}
