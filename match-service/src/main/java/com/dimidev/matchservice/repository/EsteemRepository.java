package com.dimidev.matchservice.repository;

import com.dimidev.matchservice.model.Esteem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EsteemRepository extends JpaRepository<Esteem, Long> {

    List<Esteem> findByLikerId(Long id);

    Optional<Esteem> findByIdAndLikerId(Long likeId, Long userId);
}
