package io.playce.migrator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LibType {
    JAR("jar"),
    WAR("war"),
    ;

    private final String lower;

    public static boolean isLibrary(String fileName) {
        for(LibType type: LibType.values()) {
            if(fileName.endsWith(type.getLower())) {
                return true;
            }
        }
        return false;
    }
}
