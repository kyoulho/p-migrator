package io.playce.migrator.domain.credential.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.Credential;
import io.playce.migrator.dao.repository.CredentialRepository;
import io.playce.migrator.dto.credential.CredentialRequest;
import io.playce.migrator.dto.credential.CredentialResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.GeneralCipherUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CredentialServiceTest {
    @Autowired
    CredentialService service;
    @Autowired
    CredentialRepository repository;


    @Test
    @DisplayName("잘못된 요청은 예외를 발생시킨다.")
    void regitsCredential() {
        CredentialRequest request = new CredentialRequest();
        request.setUsername("admin");
        request.setPasswordYn(true);

        PlayceMigratorException exception = assertThrows(PlayceMigratorException.class, () -> service.registCredential(request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PM107H);
    }

    @Test
    @DisplayName("credential 정보를 keyContent 로 수정한다.")
    void modifyCredential() {
        // given
        Credential credential = new Credential();
        credential.setUsername("admin");
        String password = GeneralCipherUtil.encrypt("admin");
        credential.setPassword(password);
        credential.setKeyContent(null);

        repository.save(credential);
        long id = credential.getCredentialId();

        // when
        CredentialRequest request = new CredentialRequest();
        request.setUsername("admin");
        request.setPassword(null);
        String encryptKeyContent = GeneralCipherUtil.encrypt("keyContent");
        request.setKeyContent(encryptKeyContent);
        request.setPasswordYn(false);

        service.modifyCredential(id, request);

        // then

        CredentialResponse response = service.getCredential(id);

        assertThat(response.getPasswordYn()).isFalse();
        assertThat(response.getKeyContent()).isEqualTo(encryptKeyContent);
    }
}