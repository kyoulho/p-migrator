package io.playce.migrator.domain.analysis.service;

import io.playce.migrator.analysis.process.service.AnalysisRuleService;
import io.playce.migrator.dto.analysis.AnalysisRuleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AnalysisRuleServiceTest {

    @Autowired
    AnalysisRuleService ruleService;

    @Test
    @DisplayName("타겟 아이디로 룰을 조회한다.")
    void getRulesByTargetId() {
        List<AnalysisRuleResponse> rules = ruleService.getRulesByTargetId(1L);
        assert rules.size() > 0;
    }
}