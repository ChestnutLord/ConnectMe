package com.dimidev.matchservice.service;

import com.dimidev.matchservice.model.Match;
import com.dimidev.matchservice.repository.MatchRepository;
import com.dimidev.matchservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository rep;

    public List<Match> getAllByUserId(Long userId) {
        return rep.findByUser1IdOrUser2Id(userId, userId);
    }

    public Match getByIdAndUserId(Long id, Long userId) {
        return rep.findByIdAndUser1IdOrIdAndUser2Id(id, userId, id, userId)
                .orElseThrow(() -> new NotFoundException(id, Match.class));
    }

    public Match create(Match match) {
        return rep.save(match);
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
    }
}
