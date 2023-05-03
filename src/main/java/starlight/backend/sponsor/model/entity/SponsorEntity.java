package starlight.backend.sponsor.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;
import starlight.backend.kudos.model.entity.KudosEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Validated
public class SponsorEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long sponsorId;
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @URL
    private String avatar;
    private String company;
    private Integer unusedKudos;
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;
    @OneToMany(mappedBy = "owner")
    @JsonManagedReference
    private Set<KudosEntity> kudos;
}
