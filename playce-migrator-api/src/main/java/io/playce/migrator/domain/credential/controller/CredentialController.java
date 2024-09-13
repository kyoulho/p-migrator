package io.playce.migrator.domain.credential.controller;


import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.credential.service.CredentialService;
import io.playce.migrator.dto.credential.CredentialRequest;
import io.playce.migrator.dto.credential.CredentialResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/credential")
@RestController
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    private void validate(BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new PlayceMigratorException(ErrorCode.PM107H, "An error occurred while binding data.");
        }

    }

    @GetMapping("/{credentialId}")
    CredentialResponse getCredential(@PathVariable Long credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @PostMapping
    CredentialResponse registCredential(@RequestBody @Validated CredentialRequest request, BindingResult bindingResult){
        validate(bindingResult);
        Long credentialId = credentialService.registCredential(request);
        return credentialService.getCredential(credentialId);
    }

    @PutMapping("/{credentialId}")
    CredentialResponse modifyCredential(@PathVariable Long credentialId, @RequestBody @Validated CredentialRequest request, BindingResult bindingResult) {
        validate(bindingResult);
        credentialService.modifyCredential(credentialId,request);
        return credentialService.getCredential(credentialId);
    }
}
