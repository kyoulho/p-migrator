package io.playce.migrator.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "project")
@Setter @Getter
public class Project{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String projectName;
    private String description;
    private Long targetId;
    private String registLoginId;
    private Instant registDatetime;
    private String modifyLoginId;
    private Instant modifyDatetime;
}
