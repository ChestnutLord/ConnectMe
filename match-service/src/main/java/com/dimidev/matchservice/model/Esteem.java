package com.dimidev.matchservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "esteems")
@NoArgsConstructor
@AllArgsConstructor
public class Esteem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "liker_id", nullable = false)
    private Long likerId;

    @Column(name = "liked_id", nullable = false)
    private Long likedId;

    @Column(nullable = false)
    private boolean esteem;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
