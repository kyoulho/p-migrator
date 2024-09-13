package io.playce.migrator.analysis.process.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dto.analysis.AnalysisRuleRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.dto.analysisreport.IssueDetail;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSession;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnalysisRuleSimulator {

    public List<IssueDetail> doSimulation(String ruleContent, String targetString) {
        RuleSession ruleSession = newRuleSessionForSimulation(ruleContent);
        ArrayList<IssueDetail> issueDetails = new ArrayList<>();

        int lineNumber = 0;
        for (String line : targetString.split("\n")) {
            AnalysisRuleRequest request = new AnalysisRuleRequest();
            request.setStartLineNumber(++lineNumber);
            request.setContent(line);
            ruleSession.execute(request);

            AnalysisRuleResult result = request.getResult();
            if (ObjectUtils.isNotEmpty(result)) {
                IssueDetail issueDetail = new IssueDetail();
                issueDetail.setAnalysisLineNumber(result.getLineNumber().longValue());
                issueDetail.setDetectedString(result.getDetectedString());
                issueDetail.setOverrideComment(result.getOverrideComment());

                issueDetails.add(issueDetail);
            }
        }
        return issueDetails;
    }

    private RuleSession newRuleSessionForSimulation(String ruleContent) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        Resource resource = ResourceFactory.newReaderResource(new StringReader(setRuleId(ruleContent)));
        builder.add(resource, ResourceType.DRL);

        KieBase kieBase;
        try {
            kieBase = builder.newKieBase();
            return new RuleSessionFactory(kieBase).newSession();
        } catch (IllegalArgumentException e) {
            throw new PlayceMigratorException(ErrorCode.PM703T, e);
        }
    }

    private String setRuleId(String content) {
        return content.replace("${ruleId}", 1 + "L");
    }
}
