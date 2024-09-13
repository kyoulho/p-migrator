package io.playce.migrator.domain.target.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.AnalysisProcessingPolicy;
import io.playce.migrator.dao.entity.Target;
import io.playce.migrator.dao.entity.TargetAnalysisProcessingPolicyLink;
import io.playce.migrator.dao.entity.id.TargetAnalysisProcessingPolicyLinkId;
import io.playce.migrator.dao.repository.AnalysisProcessingPolicyRepository;
import io.playce.migrator.dao.repository.TargetAnalysisProcessingPolicyLinkRepository;
import io.playce.migrator.dao.repository.TargetRepository;
import io.playce.migrator.dto.target.TargetRequest;
import io.playce.migrator.dto.target.TargetResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TargetService {
    private final TargetRepository targetRepository;
    private final TargetAnalysisProcessingPolicyLinkRepository targetAnalysisProcessingPolicyLinkRepository;

    private final AnalysisProcessingPolicyRepository analysisProcessingPolicyRepository;
    private final ModelMapper modelMapper;
    private final LoginUserUtil loginUserUtil;

    private Long registTarget(TargetRequest request) {
        Target target = modelMapper.map(request, Target.class);

        target.setRegistLoginId(loginUserUtil.getLoginId());
        target.setRegistDatetime(Instant.now());
        target.setModifyLoginId(loginUserUtil.getLoginId());
        target.setModifyDatetime(Instant.now());

        Long targetId = targetRepository.save(target).getTargetId();

        allLink(targetId);
        return targetId;
    }

    /* todo
        향수 변경 사항 있음
        타겟 생성 시 모든 룰 그룹을 링크시킨다.
    */
    private void allLink(Long targetId) {
        List<AnalysisProcessingPolicy> analysisProcessingPolicies = analysisProcessingPolicyRepository.findAll();
        for (AnalysisProcessingPolicy ruleGroup : analysisProcessingPolicies) {
            TargetAnalysisProcessingPolicyLinkId targetAnalysisProcessingPolicyLinkId = new TargetAnalysisProcessingPolicyLinkId();
            targetAnalysisProcessingPolicyLinkId.setTargetId(targetId);
            targetAnalysisProcessingPolicyLinkId.setAnalysisProcessingPolicyId(ruleGroup.getAnalysisProcessingPolicyId());

            TargetAnalysisProcessingPolicyLink targetAnalysisProcessingPolicyLink = new TargetAnalysisProcessingPolicyLink();
            targetAnalysisProcessingPolicyLink.setId(targetAnalysisProcessingPolicyLinkId);

            targetAnalysisProcessingPolicyLinkRepository.save(targetAnalysisProcessingPolicyLink);
        }
    }

    public TargetResponse getTarget(Long targetId) {
        Target target = getEntity(targetId);

        return modelMapper.map(target, TargetResponse.class);
    }

    public TargetResponse getTarget(TargetRequest request) {
        Optional<Target> optional = targetRepository.findByOsNameAndMiddlewareNameAndMiddlewareVersionAndJavaVersion(
                request.getOsName(),
                request.getMiddlewareName(),
                request.getMiddlewareVersion(),
                request.getJavaVersion()
        );

        if (optional.isEmpty()) {
            Long registTargetId = registTarget(request);
            return getTarget(registTargetId);
        } else {
            Target target = optional.get();
            return modelMapper.map(target, TargetResponse.class);
        }
    }

    private Target getEntity(Long targetId) {
        return targetRepository.findById(targetId)
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R, "target ID " + targetId + " not exist"));
    }
}
