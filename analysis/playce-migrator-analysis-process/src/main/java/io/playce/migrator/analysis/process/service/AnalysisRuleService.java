package io.playce.migrator.analysis.process.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.AnalysisProcessingPolicy;
import io.playce.migrator.dao.entity.AnalysisRule;
import io.playce.migrator.dao.mapper.AnalysisRuleMapper;
import io.playce.migrator.dao.repository.AnalysisProcessingPolicyRepository;
import io.playce.migrator.dao.repository.AnalysisRuleRepository;
import io.playce.migrator.dto.analysis.AnalysisRuleCreateRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResponse;
import io.playce.migrator.dto.analysis.AnalysisRuleSimulationRequest;
import io.playce.migrator.dto.analysisreport.IssueDetail;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisRuleService {
    private final AnalysisRuleRepository analysisRuleRepository;
    private final AnalysisProcessingPolicyRepository analysisProcessingPolicyRepository;
    private final AnalysisRuleMapper analysisRuleMapper;
    private final ModelMapper modelMapper;
    private final LoginUserUtil loginUserUtil;
    private final AnalysisRuleSimulator analysisRuleSimulator;

    public List<AnalysisRuleResponse> getRulesByTargetId(Long targetId) {
        return analysisRuleMapper.selectRulesByTargetId(targetId);
    }

    public List<AnalysisRuleResponse> getAnalysisRules() {
        return analysisRuleMapper.selectRulesWithRuleGroups();
    }

    public AnalysisRuleResponse getAnalysisRule(Long analysisRuleId) {
        return analysisRuleMapper.selectRuleWithRuleGroup(analysisRuleId);
    }

    public Long createAnalysisRule( AnalysisRuleCreateRequest analysisRuleCreateRequest) {
        AnalysisProcessingPolicy analysisProcessingPolicy = getAnalysisProcessingPolicyEntity(analysisRuleCreateRequest.getProcessingType());

        AnalysisRule analysisRule = modelMapper.map(analysisRuleCreateRequest, AnalysisRule.class);
        analysisRule.setAnalysisProcessingPolicy(analysisProcessingPolicy);
        analysisRule.setRegistLoginId(loginUserUtil.getLoginId());
        analysisRule.setRegistDatetime(Instant.now());
        analysisRule.setModifyLoginId(loginUserUtil.getLoginId());
        analysisRule.setModifyDatetime(Instant.now());
        return analysisRuleRepository.save(analysisRule).getAnalysisRuleId();
    }

    public AnalysisProcessingPolicy getAnalysisProcessingPolicyEntity(String processingType) {
        return analysisProcessingPolicyRepository.findByProcessingType(processingType)
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R,
                        "the analysis processing policy type : " + processingType + " not found."));
    }

    @Transactional
    public void removeAnalysisRule(Long analysisRuleId) {
        AnalysisRule analysisRule = getAnalysisRuleEntity(analysisRuleId);
        analysisRule.setDeleteYn(true);
    }

    private AnalysisRule getAnalysisRuleEntity(Long analysisRuleId) {
        return analysisRuleRepository.findById(analysisRuleId).orElseThrow(
                () -> new PlayceMigratorException(ErrorCode.PM201R,
                        "the analysis rule id : " + analysisRuleId + "not found."));
    }

    public List<IssueDetail> simulateAnalysisRule(AnalysisRuleSimulationRequest request) {
        return analysisRuleSimulator.doSimulation(request.getRuleContent(), request.getTargetString());
    }
}
