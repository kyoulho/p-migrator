package io.playce.migrator.domain.authentication.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.ErrorType;
import io.playce.migrator.dto.exception.ErrorResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (PlayceMigratorException e) {
            sendErrorResponse(request, response, e);
        }
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, PlayceMigratorException e) throws IOException {
        ErrorCode errorCode = e.getErrorCode();
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        String defaultMessage = messageSource.getMessage(ErrorType.messageCode(errorCode), null, e.getMessage(), request.getLocale());
        ErrorResponse errorResponse = new ErrorResponse(errorCode.name(), defaultMessage, sw.toString());
        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
