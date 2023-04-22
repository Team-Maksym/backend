package starlight.backend.proof.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.user.model.entity.UserEntity;

import java.time.Instant;
import java.util.Set;

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

    private Instant dateLastUpdated;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "proof",fetch = FetchType.EAGER)
    private Set<KudosEntity> kudos;

    public void setDateLastUpdated(Instant now) {
        this.dateLastUpdated = now;
    }
}
