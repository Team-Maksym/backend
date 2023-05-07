package starlight.backend.advice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long entityID;
    @NotNull
    private UUID userDeletingProcessUUID;
    @NotNull
    private Instant deleteDate;
    @NotNull
    private DeletingEntityType deletingEntityType;

}
