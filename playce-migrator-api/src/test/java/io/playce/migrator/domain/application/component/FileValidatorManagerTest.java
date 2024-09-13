package io.playce.migrator.domain.application.component;

import io.playce.migrator.domain.application.component.upload.FileValidatorManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileValidatorManagerTest {
    @Autowired
    private FileValidatorManager fileValidatorManager;

    @Test
    void validateEar() {
        boolean result = fileValidatorManager.validate(new File("test-files/ear/petstore.ear"));
        assertThat(result).isTrue();
    }

    @Test
    void validateWar() {
        boolean result = fileValidatorManager.validate(new File("test-files/war/batch10-joboperator.war"));
        assertThat(result).isTrue();
    }

    @Test
    void validateJar() {
        boolean result = fileValidatorManager.validate(new File("test-files/jar/spring-boot-sample.jar"));
        assertThat(result).isTrue();
    }

    @Test
    void validateZip() {
        boolean result = fileValidatorManager.validate(new File("test-files/zip/sample.zip"));
        assertThat(result).isTrue();
    }
}