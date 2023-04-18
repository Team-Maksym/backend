package starlight.backend.kudos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class KudosEntity {
    @Id 
    @GeneratedValue(strategy = IDENTITY)
    private Long kudosId;
    private Long proofId;
    private Long followerId;
    private Instant createData;

    @OneToOne
    @MapsId
    @JoinColumn(name ="owner")
    private UserEntity owner;
}
