package com.dimidev.matchservice.repository;

import com.dimidev.matchservice.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);


    Optional<Match> findByIdAndUser1IdOrIdAndUser2Id(Long matchId1, Long userId1, Long matchId2, Long userId2);

    Optional<Match> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
