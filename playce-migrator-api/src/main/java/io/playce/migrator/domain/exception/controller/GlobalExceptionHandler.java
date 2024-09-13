package io.playce.migrator.domain.exception.controller;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.ErrorType;
import io.playce.migrator.dto.exception.ErrorResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.UnexpectedTypeException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        log.error("MethodArgumentNotValidException occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM107H, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, WebRequest request) {
        log.error("MethodNotSupportedException occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM104H, e);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, WebRequest request) {
        log.error("HttpMediaTypeNotSupportedException occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM105H, e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleException(DataIntegrityViolationException e, WebRequest request) {
        log.error("DataIntegrityViolationException occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM401D, e);
    }

    @ExceptionHandler({IllegalArgumentException.class, MissingRequestValueException.class, MissingServletRequestPartException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception e, WebRequest request) {
        log.error("IllegalArgumentException occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM107H, e);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    protected ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        log.error("Unhandled exception occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM106H, e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(Exception e, WebRequest request) {
        log.error("HttpMessageNotReadableException exception occurred while execute [{}].", request.getDescription(false), e);

        return newResponseEntity(ErrorCode.PM107H, e);
    }

    @ExceptionHandler(PlayceMigratorException.class)
    protected ResponseEntity<ErrorResponse> handlePlayceMigratorException(PlayceMigratorException e, WebRequest request) {
        log.error("PlayceMigratorException occurred while execute [{}] ", request.getDescription(false), e);
        return newResponseEntity(e.getErrorCode(), e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    protected ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e, WebRequest request) {
        log.error("PlayceMigratorException occurred while execute [{}] ", request.getDescription(false), e);
        return newResponseEntity(ErrorCode.PM106H, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<ErrorResponse> handleUnexpectedTypeException(UnexpectedTypeException e, WebRequest request) {
        log.error("PlayceMigratorException occurred while execute [{}] ", request.getDescription(false), e);
        return newResponseEntity(ErrorCode.PM107H, e);
    }


    private ResponseEntity<ErrorResponse> newResponseEntity(ErrorCode errorCode, Exception e) {
        String code = errorCode.name();
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        String defaultMessage = messageSource.getMessage(ErrorType.messageCode(errorCode), null, e.getMessage(), LocaleContextHolder.getLocale());
        ErrorResponse errorResponse = new ErrorResponse(code, defaultMessage, sw.toString());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
