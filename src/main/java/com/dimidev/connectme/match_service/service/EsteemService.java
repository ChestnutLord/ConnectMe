package com.dimidev.connectme.match_service.service;

import com.dimidev.connectme.match_service.model.Esteem;
import com.dimidev.connectme.match_service.repository.EsteemRepository;
import com.dimidev.connectme.user_service.exception.NotFoundException;
import com.dimidev.connectme.user_service.service.UserService;
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
    private final UserService userService;

    public List<Esteem> getAllByUserId(Long userId) {
        return rep.findByLiker_Id(userId);
    }

    public Esteem getByIdAndUserId(Long likeId, Long userId) {
        return rep.findByIdAndLiker_Id(likeId, userId)
                .orElseThrow(() -> new NotFoundException(likeId, Esteem.class));
    }

    // при создании положительной оценки нужно проверить есть ли взаимная положительная оценка,
    // и если есть создать матча
    public Esteem create(Long likerId, Long likedId, boolean esteemValue) {
        if (likerId.equals(likedId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot evaluate yourself."
            );
        }
        if (rep.findByLiker_Id(likerId).stream()
                .anyMatch(e -> e.getLiked().getId().equals(likedId))) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An esteem between these users already exists. Use the update method to modify it."
            );
        }

        Esteem newEsteem = new Esteem();
        newEsteem.setLiker(userService.getById(likerId));
        newEsteem.setLiked(userService.getById(likedId));
        newEsteem.setEsteem(esteemValue);

        rep.save(newEsteem);

        // Если оценка положительная — проверяем взаимность
        if (esteemValue) {
            if (rep.findByLiker_Id(likedId).stream()
                    .anyMatch(e -> e.getLiked().getId().equals(likerId) && e.isEsteem())) {
                matchService.sortAndCreate(likerId, likedId);
            }
        }

        return newEsteem;
    }


    // при изменении оценки на положительную должна идти проверка на взаимность
    // если на отрицательную то нужно проверить есть ли матч и удалить его

    public Esteem update(Long id, Long userId, boolean newEsteem) {

        Esteem existing = getByIdAndUserId(id, userId);

        if (!existing.getLiker().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update someone else's esteem."
            );
        }

        existing.setEsteem(newEsteem);
        rep.save(existing);

        Long likedId = existing.getLiked().getId();

        if (newEsteem) {
            if (rep.findByLiker_Id(likedId).stream()
                    .anyMatch(e -> e.getLiked().getId().equals(userId) && e.isEsteem())) {
                matchService.sortAndCreate(userId, likedId);
            }
        } else {
            if (rep.findByLiker_Id(likedId).stream()
                    .anyMatch(e -> e.getLiked().getId().equals(userId) && e.isEsteem())) {
                matchService.deleteByUserIds(userId, likedId);
            }
        }

        return existing;
    }


    // при удалении положительной оценки удаляться матч
    public void deleteByIdAndUserId(Long id, Long userId) {
        rep.findByIdAndLiker_Id(id, userId).orElseThrow(() -> new NotFoundException(id, Esteem.class)); // TODO Это вообще работает???
        Esteem esteem = getByIdAndUserId(id, userId);
        if (esteem.isEsteem()) {
            if (rep.findByLiker_Id(esteem.getLiked().getId()).stream()
                    .anyMatch(e -> e.getLiked().equals(id) && e.isEsteem())) {
                matchService.deleteByUserIds(userId, esteem.getLiked().getId());
            }
        }

        rep.deleteById(id);
    }
}
