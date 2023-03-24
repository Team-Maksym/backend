package starlight.backend.talent.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
public class TalentEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long talentId;
    @NotNull
    private String fullName;
    @NotNull
    @Email
    private String mail;
    @NotNull
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
    @JsonManagedReference
    private Set<PositionEntity> positions;


    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;
}