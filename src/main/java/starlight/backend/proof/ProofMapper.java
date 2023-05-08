package starlight.backend.proof;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.response.*;

import java.util.LinkedList;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProofMapper {
    default ProofInfo toProofInfo(ProofEntity proof) {
        return ProofInfo.builder()
                .id(proof.getProofId())
                .dateCreated(proof.getDateCreated())
                .description(proof.getDescription())
                .title(proof.getTitle())
                .status(proof.getStatus())
                .build();
    }

    default ProofPagePagination toProofPagePagination(Page<ProofEntity> proofs) {
        return ProofPagePagination.builder()
                .total(proofs.getTotalElements())
                .data(proofs.getContent()
                        .stream()
                        .map(this::toProofInfo)
                        .toList())
                .build();
    }

    default ProofPagePagination toProofPagePaginationWithProofFullInfo(Page<ProofEntity> proofs) {
        return ProofPagePagination.builder()
                .total(proofs.getTotalElements())
                .data(proofs.getContent()
                        .stream()
                        .map(this::toProofFullInfo)
                        .toList())
                .build();
    }
    default ProofPagePagination toProofPagePaginationWithProofFullInfoV2(Page<ProofEntity> proofs){
        return ProofPagePagination.builder()
                .total(proofs.getTotalElements())
                .data(proofs.getContent()
                        .stream()
                        .map(this::toProofFullInfoV2)
                        .toList())
                .build();
    }
    default ProofFullInfoV2 toProofFullInfoV2(ProofEntity proof) {
        return ProofFullInfoV2.builder()
                .id(proof.getProofId())
                .title(proof.getTitle())
                .link(proof.getLink())
                .status(proof.getStatus())
                .dateCreated(proof.getDateCreated())
                .dateLastUpdated(proof.getDateLastUpdated())
                .description(proof.getDescription())
                .sponsorForProofShortInfoList(
                        new LinkedList<>(
                                SponsorForProofShortInfo.listBuilder(proof)
                        )
                )
                .build();
    }

    default ProofFullInfo toProofFullInfo(ProofEntity proof) {
        return ProofFullInfo.builder()
                .id(proof.getProofId())
                .title(proof.getTitle())
                .link(proof.getLink())
                .status(proof.getStatus())
                .dateCreated(proof.getDateCreated())
                .dateLastUpdated(proof.getDateLastUpdated())
                .description(proof.getDescription())
                .build();
    }
}
