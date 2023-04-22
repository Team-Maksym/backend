package starlight.backend.kudos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.user.model.entity.UserEntity;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class KudosEntity {
    @Id 
    @GeneratedValue(strategy = IDENTITY)
    private Long kudosId;
    private Long followerId;
    private Instant createData;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proof_id", nullable = false)
    private ProofEntity proof;
}
