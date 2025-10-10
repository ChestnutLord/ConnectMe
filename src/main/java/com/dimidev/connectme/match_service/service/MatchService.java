package com.dimidev.connectme.match_service.service;

import com.dimidev.connectme.match_service.model.Match;
import com.dimidev.connectme.match_service.repository.MatchRepository;
import com.dimidev.connectme.user_service.exception.NotFoundException;
import com.dimidev.connectme.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository rep;
    private final UserService userService;

    public List<Match> getAllByUserId(Long userId) {
        return rep.findByUser1_IdOrUser2_Id(userId, userId);
    }

    public Match getByIdAndUserId(Long id, Long userId) {
        return rep.findByIdAndUser1_IdOrIdAndUser2_Id(id, userId, id, userId)
                .orElseThrow(() -> new NotFoundException(id, Match.class));
    }

    public Match create(Match match) {
        return rep.save(match);
    }

    public void sortAndCreate(Long user1Id, Long user2Id) {
        Match match = new Match();
        if (user1Id < user2Id) {
            match.setUser1(userService.getById(user1Id));
            match.setUser2(userService.getById(user2Id));
        } else {
            match.setUser1(userService.getById(user2Id));
            match.setUser2(userService.getById(user1Id));
        }
        create(match);
    }

    public void deleteByUserIds(Long user1Id, Long user2Id) {
        Long first = Math.min(user1Id, user2Id);
        Long second = Math.max(user1Id, user2Id);

        Match match = rep.findByUser1_IdAndUser2_Id(first, second)
                .orElseThrow(() -> new NotFoundException(first + "," + second, Match.class));
        rep.delete(match);
    }
}
