package io.playce.migrator.dao.mapper;

import io.playce.migrator.dto.analysis.AnalysisRuleResponse;
import io.playce.migrator.dto.analysisreport.AnalysisReportRuleResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnalysisRuleMapper {

    List<AnalysisRuleResponse> selectRulesByTargetId(Long targetId);

    List<AnalysisReportRuleResponse> selectAnalysisReportRules(Long analysisHistoryId, Long analysisReportGroupId);

    List<AnalysisRuleResponse> selectRulesWithRuleGroups();

    AnalysisRuleResponse selectRuleWithRuleGroup(Long analysisRuleId);
}
