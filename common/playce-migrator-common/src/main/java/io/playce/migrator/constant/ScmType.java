package io.playce.migrator.constant;

import io.playce.migrator.exception.PlayceMigratorException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ScmType {
    GIT(".git");

    private final String requiredString;


    public static ScmType getScmType(String url) {
        for (ScmType value : ScmType.values()) {
            if (url.endsWith(value.requiredString)) {
                return value;
            }
        }
        throw new PlayceMigratorException(ErrorCode.PM801S);
    }
}
