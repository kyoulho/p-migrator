package io.playce.migrator.analysis.process.service;

import io.playce.migrator.constant.DeprecatedType;
import io.playce.migrator.dao.entity.DeletedInfo;
import io.playce.migrator.dao.entity.DependenciesInfo;
import io.playce.migrator.dao.entity.DeprecatedInfo;
import io.playce.migrator.dao.mapper.AnalysisHistoryMapper;
import io.playce.migrator.dao.mapper.AnalysisRuleMapper;
import io.playce.migrator.dao.repository.AnalysisReportGroupRepository;
import io.playce.migrator.dao.repository.DeletedInfoRepository;
import io.playce.migrator.dao.repository.DependenciesInfoRepository;
import io.playce.migrator.dao.repository.DeprecatedInfoRepository;
import io.playce.migrator.dto.analysisreport.*;
import io.playce.migrator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisReportService {
    private final AnalysisHistoryMapper analysisHistoryMapper;

    private final AnalysisRuleMapper analysisRuleMapper;
    private final AnalysisReportGroupRepository analysisReportGroupRepository;
    private final DependenciesInfoRepository dependenciesInfoRepository;
    private final ModelMapper modelMapper;
    private final DeprecatedInfoRepository deprecatedInfoRepository;
    private final DeletedInfoRepository deletedInfoRepository;

    public List<AnalyzedLibraries> getAnalyzedDependencies(Long applicationId) {
        List<DependenciesInfo> dependenciesInfos = dependenciesInfoRepository.findByApplicationId(applicationId);
        return groupByLanguage(dependenciesInfos);
    }

    public List<AnalysisReportGroupResponse> getAnalysisReportGroups() {
        return analysisReportGroupRepository.findAll()
                .stream().map(i -> modelMapper.map(i, AnalysisReportGroupResponse.class))
                .collect(Collectors.toList());
    }

    public List<AnalysisReportRuleResponse> getAnalysisReportRules(Long analysisHistoryId, Long analysisReportGroupId) {
        return analysisRuleMapper.selectAnalysisReportRules(analysisHistoryId, analysisReportGroupId);
    }

    public List<AnalysisReportRuleResult> getAnalysisReportRuleResults(Long analysisHistoryId, Long analysisReportGroupId) {
        return analysisHistoryMapper.selectAnalysisReportRuleResults(analysisHistoryId, analysisReportGroupId);
    }

    private List<AnalyzedLibraries> groupByLanguage(List<DependenciesInfo> allLibraries) {
        AnalyzedLibraries javaLibraries = newAnalyzedLibraries("Java Libraries");

        for (DependenciesInfo dependenciesInfo : allLibraries) {
            String libraryPathAndName = dependenciesInfo.getFoundAtPath();
            if (libraryPathAndName.endsWith(".jar")) {
                javaLibraries.getLibraries().add(newAnalyzedLibrary(dependenciesInfo));
            }
            // 다른 언어의 라이브러리는 if-else 로 추가
        }
        return List.of(javaLibraries);
    }

    private AnalyzedLibraries newAnalyzedLibraries(String groupName) {
        AnalyzedLibraries analyzedLibraries = new AnalyzedLibraries();
        analyzedLibraries.setGroupName(groupName);
        analyzedLibraries.setLibraries(new ArrayList<>());
        return analyzedLibraries;
    }

    private AnalyzedLibrary newAnalyzedLibrary(DependenciesInfo dependenciesInfo) {
        AnalyzedLibrary analyzedLibrary = new AnalyzedLibrary();
        analyzedLibrary.setFileDetails(newFileDetails(dependenciesInfo));
        analyzedLibrary.setFileName(FileUtil.getName(dependenciesInfo.getFoundAtPath()));
        return analyzedLibrary;
    }

    private FileDetails newFileDetails(DependenciesInfo dependenciesInfo) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setSha1Hash(dependenciesInfo.getSha1Value());
        fileDetails.setFoundAtPath(dependenciesInfo.getFoundAtPath());
        fileDetails.setDescription(dependenciesInfo.getDescription());
        return fileDetails;
    }

    public List<DeprecatedApiResponse> getDeprecatedApis(Long applicationId) {
        return deprecatedInfoRepository.findAllByApplicationId(applicationId)
                .stream().map(this::generateDeprecateApis).collect(Collectors.toList());
    }

    public List<DeletedApiResponse> getDeletedApis(Long applicationId) {
        return deletedInfoRepository.findAllByApplicationId(applicationId)
                .stream().map(this::generateDeletedApis).collect(Collectors.toList());
    }

    private DeprecatedApiResponse generateDeprecateApis(DeprecatedInfo deprecatedInfo) {
        DeprecatedApiResponse response = new DeprecatedApiResponse();
        response.setClassName(deprecatedInfo.getUsedClassName());
        if (DeprecatedType.METHOD.name().equals(deprecatedInfo.getDeprecatedType())) {
            response.setJdkInternalApi(deprecatedInfo.getDeprecatedName().split("::")[0]);
            response.setMethod(deprecatedInfo.getDeprecatedName().split("::")[1]);
        } else {
            response.setJdkInternalApi(deprecatedInfo.getDeprecatedName());
            response.setMethod("N/A");
        }
        response.setForRemoval(deprecatedInfo.getForRemoval());
        return response;
    }

    private DeletedApiResponse generateDeletedApis(DeletedInfo deletedInfo) {
        DeletedApiResponse response = new DeletedApiResponse();
        response.setClassName(deletedInfo.getUsedClassName());
        response.setJdkInternalApi(deletedInfo.getDeletedName());
        response.setReplacement(deletedInfo.getReplacementComment());
        return response;
    }

    public List<SummaryTechnology> getSummaryTechnologies(Long analysisHistoryId) {
        return analysisHistoryMapper.selectSummaryTechnology(analysisHistoryId);
    }

    public List<SummaryExtension> getSummaryExtensions(Long applicationId) {
        return analysisHistoryMapper.selectSummaryExtension(applicationId);
    }
}
