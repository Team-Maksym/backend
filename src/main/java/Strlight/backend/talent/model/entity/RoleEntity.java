package Strlight.backend.talent.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Builder
@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoleEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long roleId;

    @ManyToMany(mappedBy = "talentRole")
    private Set<TalentEntity> role;
}