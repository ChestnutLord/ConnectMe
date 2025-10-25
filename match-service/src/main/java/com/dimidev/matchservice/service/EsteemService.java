package com.dimidev.matchservice.service;

import com.dimidev.matchservice.client.UserClient;
import com.dimidev.matchservice.model.Esteem;
import com.dimidev.matchservice.repository.EsteemRepository;
import com.dimidev.matchservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EsteemService {

    private final EsteemRepository rep;
    private final MatchService matchService;
    private final UserClient userClient;

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

        if (!existing.getLikerId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update someone else's esteem."
            );
        }

        existing.setEsteem(newEsteem);
        rep.save(existing);

        Long likedId = existing.getLikedId();

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
        rep.findByIdAndLikerId(id, userId).orElseThrow(() -> new NotFoundException(id, Esteem.class)); // TODO does it work???
        Esteem esteem = getByIdAndUserId(id, userId);
        if (esteem.isEsteem()) {
            if (rep.findByLikerId(esteem.getLikedId()).stream()
                    .anyMatch(e -> e.getLikedId().equals(id) && e.isEsteem())) {
                matchService.deleteByUserIds(userId, esteem.getLikedId());
            }
        }

        rep.deleteById(id);
    }
}
