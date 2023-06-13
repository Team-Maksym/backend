package starlight.backend.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import starlight.backend.sponsor.model.entity.SponsorEntity;
import starlight.backend.talent.model.entity.TalentEntity;

import java.util.Collection;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Validated
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "talent_id")
    private TalentEntity talent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sponsor_id")
    private SponsorEntity sponsor;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}
