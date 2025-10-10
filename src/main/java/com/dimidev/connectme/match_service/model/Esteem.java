package com.dimidev.connectme.match_service.model;

import com.dimidev.connectme.user_service.model.User;
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

    @ManyToOne
    @JoinColumn(name = "liker_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_liker"))
    private User liker;

    @ManyToOne
    @JoinColumn(name = "liked_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_liked"))
    private User liked;

    @Column(nullable = false)
    private boolean esteem;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
