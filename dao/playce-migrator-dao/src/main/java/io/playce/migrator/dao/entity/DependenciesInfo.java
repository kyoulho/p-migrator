package io.playce.migrator.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "dependencies_info")
public class DependenciesInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dependenciesInfoId;
    @Column(name = "sha1_value")
    private String sha1Value;
    private String foundAtPath;
    private String description;
    private Long applicationId;
}
