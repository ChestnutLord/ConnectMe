package com.dimidev.connectme.match_service.repository;

import com.dimidev.connectme.match_service.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUser1_IdOrUser2_Id(Long user1Id, Long user2Id);


    Optional<Match> findByIdAndUser1_IdOrIdAndUser2_Id(Long matchId1, Long userId1, Long matchId2, Long userId2);

    Optional<Match> findByUser1_IdAndUser2_Id (Long user1Id, Long user2Id);
}
