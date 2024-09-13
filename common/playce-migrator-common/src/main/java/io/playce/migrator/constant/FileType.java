package io.playce.migrator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.nio.file.Path;

@Getter
@AllArgsConstructor
public enum FileType {
    JAVA( "lineRuleCheckProvider"),
    JSP("lineRuleCheckProvider"),
    XML("lineRuleCheckProvider"),
    PROPERTIES("lineRuleCheckProvider"),
    YAML("lineRuleCheckProvider"),
    YML("lineRuleCheckProvider"),
    CLASS("lineRuleCheckProvider"),
    JAR_DEP("jarDependencyRuleCheckProvider"),
    ;

    private final String provider;

    public static boolean check(Path fileName) {
        for(FileType type: FileType.values()) {
            if(fileName.toString().toUpperCase().endsWith(type.name())) {
                return true;
            }
        }
        return false;
    }

    public String lower() {
        return name().toLowerCase();
    }

    public String getKey(Long targetId) {
        return targetId + File.separator + name();
    }
}
