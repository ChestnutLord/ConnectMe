package com.dimidev.connectme.match_service.repository;

import com.dimidev.connectme.match_service.model.Esteem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EsteemRepository extends JpaRepository<Esteem, Long> {

    List<Esteem> findByLiker_Id(Long id);

    Optional<Esteem> findByIdAndLiker_Id(Long likeId, Long userId);
}
