package starlight.backend.proof.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public class ProofListWithSkills {
    List<ProofFullInfoWithSkills> data;
}
