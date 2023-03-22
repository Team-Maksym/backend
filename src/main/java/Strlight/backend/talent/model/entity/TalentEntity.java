package Strlight.backend.talent.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TalentEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long talentId;
    private String fullName;
    private String mail;
    private String password;
    private Integer age;
    private String avatarUrl;
    private String education;
    private String experience;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "talent_position",
            joinColumns = @JoinColumn(name = "talent_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id"))
    private Set<PositionEntity> positions;

    @ManyToMany @JoinTable(
            name = "talent_roles",
            joinColumns = @JoinColumn(name = "talent_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> talentRole;
}