package starlight.backend.proof.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;
import starlight.backend.user.model.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Collection;

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

    @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]"
            , message = "The date must be in the format: yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
