package io.playce.migrator.domain.authentication.jwt.extractor;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static io.playce.migrator.constant.CommonConstants.AUTHENTICATION_TYPE_BEARER;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

    @Override
    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new PlayceMigratorException(ErrorCode.PM504A);
        }

        if (header.length() < AUTHENTICATION_TYPE_BEARER.length()) {
            throw new PlayceMigratorException(ErrorCode.PM505A);
        }

        return header.substring(AUTHENTICATION_TYPE_BEARER.length());
    }
}
