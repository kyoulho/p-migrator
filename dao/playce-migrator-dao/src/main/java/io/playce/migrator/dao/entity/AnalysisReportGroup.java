package io.playce.migrator.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "analysis_report_group")
public class AnalysisReportGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisReportGroupId;
    private String analysisReportGroupName;
}
