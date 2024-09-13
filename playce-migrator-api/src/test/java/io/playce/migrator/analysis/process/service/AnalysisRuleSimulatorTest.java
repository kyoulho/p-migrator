package io.playce.migrator.analysis.process.service;

import io.playce.migrator.dao.entity.AnalysisRule;
import io.playce.migrator.dao.repository.AnalysisRuleRepository;
import io.playce.migrator.dto.analysisreport.IssueDetail;
import io.playce.migrator.exception.PlayceMigratorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AnalysisRuleSimulatorTest {
    @Autowired
    private AnalysisRuleSimulator simulator;

    @Autowired
    private AnalysisRuleRepository ruleRepository;

    @Test
    @DisplayName("룰 시뮬레이션에 성공한다.")
    void doSimulation() {
        AnalysisRule analysisRule = ruleRepository.findById(32L).get();
        String ruleContent = analysisRule.getAnalysisRuleContent();
        String targetString = "asd\n123.111.123.123\n777.777.777.777";
        List<IssueDetail> issueDetails = simulator.doSimulation(ruleContent, targetString);
        assertThat(issueDetails).hasSize(2);
    }

    @Test
    @DisplayName("룰 시뮬레이션에 실패한다.")
    void doSimulationWithWrongTarget() {
        AnalysisRule analysisRule = ruleRepository.findById(32L).get();
        String ruleContent = analysisRule.getAnalysisRuleContent();
        String targetString = "asdf\n 123123\n asdfasgdds";
        List<IssueDetail> issueDetails = simulator.doSimulation(ruleContent, targetString);
        assertThat(issueDetails).isEmpty();
    }

    @Test
    @DisplayName("잘못된 Drools 문법은 시뮬레이션에 실패한다.")
    void doSimulationWithWrongRule() {
        String fakeRule = "fake";
        String targetString = "asdf\n 111.111.111.111\n 222.222.222.222";
        assertThrows(PlayceMigratorException.class, () -> simulator.doSimulation(fakeRule, targetString));
    }
}