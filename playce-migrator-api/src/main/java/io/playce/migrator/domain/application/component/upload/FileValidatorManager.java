package io.playce.migrator.domain.application.component.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileValidatorManager {
    private final Map<String, IFileValidator> fileValidatorMap;

    public boolean validate(File file) {
        for (IFileValidator fileValidator : fileValidatorMap.values()) {
            if (fileValidator.isSupport(file) && fileValidator.validate(file)) {
                return true;
            }
        }
        return false;
    }
}
