package starlight.backend.advice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import starlight.backend.advice.model.enums.DeletingEntityType;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Validated
public class DelayedDeleteEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long ID;
    @NotBlank
    private Long entityID;
    @NotBlank
    private UUID userDeletingProcessUUID;
    @NotBlank
    private Instant deleteDate;
    @NotBlank
    private DeletingEntityType deletingEntityType;

}
