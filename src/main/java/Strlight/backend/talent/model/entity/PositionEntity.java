package Strlight.backend.talent.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Builder
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id_position;

    String position;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "talent_id", nullable = false)
    @JsonBackReference
    TalentEntity talent;
}

