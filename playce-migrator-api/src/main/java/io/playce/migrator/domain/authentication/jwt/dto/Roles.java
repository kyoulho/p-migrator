package io.playce.migrator.domain.authentication.jwt.dto;

public enum Roles {

    REFRESH_TOKEN;

    /**
     * Authority string.
     *
     * @return the string
     */
    public String authority() {
        return "ROLE_" + this.name();
    }
}
