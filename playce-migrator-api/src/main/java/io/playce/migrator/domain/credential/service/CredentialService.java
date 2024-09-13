package io.playce.migrator.domain.credential.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.Credential;
import io.playce.migrator.dao.repository.CredentialRepository;
import io.playce.migrator.dto.credential.CredentialRequest;
import io.playce.migrator.dto.credential.CredentialResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.GeneralCipherUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.playce.migrator.util.ObjectChecker.requireNonNull;
import static io.playce.migrator.util.ObjectChecker.requireTrue;

@Service
@Transactional
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialRepository repository;
    private final ModelMapper modelMapper;

    private Credential getEntity(Long credentialId) {
        return repository.findById(credentialId)
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R, "Credential Id " + credentialId + "is not exist"));
    }

    public CredentialResponse getCredential(Long credentialId) {
        Credential credential = getEntity(credentialId);
        return modelMapper.map(credential, CredentialResponse.class);
    }

    public Long registCredential(CredentialRequest request) {
        validateCredential(request);
        Credential credential = modelMapper.map(request, Credential.class);
        return repository.save(credential).getCredentialId();
    }

    public void modifyCredential(Long credentialId, CredentialRequest request) {
        validateCredential(request);
        modelMapper.map(request, getEntity(credentialId));
    }

    private void validateCredential(CredentialRequest request) {
        if (request.getPasswordYn()) {
            String decryptPassword = GeneralCipherUtil.decrypt(request.getPassword());
            requireNonNull(decryptPassword, new PlayceMigratorException(ErrorCode.PM805S));
        } else {
            String keyContent = request.getKeyContent();
            requireTrue(keyContent.startsWith("-----BEGIN RSA PRIVATE KEY-----") && keyContent.endsWith("-----END RSA PRIVATE KEY-----"), new PlayceMigratorException(ErrorCode.PM806S));
        }
    }
}
