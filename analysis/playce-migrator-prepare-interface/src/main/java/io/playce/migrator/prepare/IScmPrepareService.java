package io.playce.migrator.prepare;

import java.nio.file.Path;

public interface IScmPrepareService {
    void cloneRepository(String url, String branch, String id, String pw, Path targetDir);

    void cloneRepository(String url, String branch, String prvKey, Path targetDir);
}
