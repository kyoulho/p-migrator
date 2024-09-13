package io.playce.migrator.domain.authentication.jwt.extractor;

public interface TokenExtractor {

    String extract(String payload);
}
