package starlight.backend.skill.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.user.model.entity.UserEntity;

import java.util.Collection;
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
public class SkillEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long skillId;

    private String skill;
    private String category;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<Long> idProofs;

    @ManyToMany(mappedBy = "skills")
    @JsonBackReference
    private Set<ProofEntity> proofs;

    @ManyToMany(mappedBy = "talentSkills")
    @JsonBackReference
    private Set<UserEntity> talents;

    public SkillEntity(String skill) {
        this.skill = skill;
    }
}