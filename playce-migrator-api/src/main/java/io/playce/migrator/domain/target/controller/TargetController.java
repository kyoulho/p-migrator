package io.playce.migrator.domain.target.controller;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.target.service.TargetService;
import io.playce.migrator.dto.target.TargetRequest;
import io.playce.migrator.dto.target.TargetResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/targets")
@RestController
@RequiredArgsConstructor
public class TargetController {

    private final TargetService targetService;

    @GetMapping("/{targetId}")
    @ResponseStatus(HttpStatus.OK)
    TargetResponse getTarget(@PathVariable Long targetId) {
        return targetService.getTarget(targetId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    TargetResponse registTarget(@RequestBody @Validated TargetRequest request, BindingResult bindingResult) {
        validate(bindingResult);
        request.setContainerizationYn(Boolean.FALSE); // 당장 사용하지 않아서 not null value 는 'false' 로 저장
        return targetService.getTarget(request);
    }

    private void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PlayceMigratorException(ErrorCode.PM107H, "An error occurred while binding data.");
        }
    }

}
