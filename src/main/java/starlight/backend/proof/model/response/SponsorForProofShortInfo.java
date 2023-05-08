package starlight.backend.proof.model.response;

import lombok.Builder;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.proof.model.entity.ProofEntity;

import java.util.LinkedList;
import java.util.Set;

@Builder
public record SponsorForProofShortInfo(
        String sponsorName,
        int countKudos,
        String sponsorAvatarUrl
) {
    public static LinkedList<SponsorForProofShortInfo> listBuilder(ProofEntity proof){
        var kudos = new LinkedList<SponsorForProofShortInfo>();

        Set<KudosEntity> kudosEntitySet = proof.getKudos();
        kudosEntitySet.forEach(kudosEntity -> kudos.add(
                SponsorForProofShortInfo.builder()
                        .sponsorName(kudosEntity.getOwner().getFullName())
                        .sponsorAvatarUrl(kudosEntity.getOwner().getAvatar())
                        .countKudos(kudosEntity.getCountKudos())
                        .build()
                )
        );
        return kudos;
    }
}
