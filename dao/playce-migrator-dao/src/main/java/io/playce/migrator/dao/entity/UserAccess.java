package io.playce.migrator.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_access")
@Getter
@Setter
public class UserAccess {

    @Id
    private String loginId;

    private String userName;
    private String password;
}
