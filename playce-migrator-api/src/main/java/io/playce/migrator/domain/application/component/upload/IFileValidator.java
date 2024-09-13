package io.playce.migrator.domain.application.component.upload;

import java.io.File;

public interface IFileValidator {

    boolean isSupport(File file);

    boolean validate(File file);
}
