package com.crud.demo.exceptions.handler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.crud.demo.exceptions.ApiException;
import com.crud.demo.exceptions.RestErrorMessage;
import com.crud.demo.exceptions.enums.MensagemExceptionEnum;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        Map<String, List<String>> response = new HashMap<>();
        response.put("erros", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<RestErrorMessage> handleApiException(ApiException ex) {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        return ResponseEntity.status(ex.getStatus())
                .body(new RestErrorMessage(ex.getStatus(), ex.getMessage(), LocalDateTime.now(zoneId)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> genericExceptionHandler(Exception ex) {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                        MensagemExceptionEnum.ERRO_INTERNO.getMensagem(),
                        LocalDateTime.now(zoneId)));
    }
}
