package starlight.backend.proof.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;
import starlight.backend.proof.model.enums.Status;
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
public class ProofEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long proofId;
    @NotBlank
    private String title;

    @Length(max = 1000)
    private String description;

    @URL
    private String link;

    private Instant dateCreated;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
