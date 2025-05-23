package com.med.backend.exception;

import com.med.backend.dto.auth.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setMessage("Error interno en el servidor, por favor intente más tarde");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(
            HttpServletRequest request, MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setHttpCode(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Error en la petición, por favor revise los datos enviados");

        System.out.println(exception.getAllErrors().stream().map(error -> error.getDefaultMessage())
                .collect(Collectors.toList())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerAccessDeniedException(
            HttpServletRequest request, AccessDeniedException exception) {

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setHttpCode(HttpStatus.FORBIDDEN.value());
        apiError.setMessage("Acceso denegado. " +
                "No tienes los permisos necesarios para acceder a esta función. " +
                "Por favor contacta con el administrador si crees que esto es un error.");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(HttpServletRequest request, DuplicateResourceException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setHttpCode(HttpStatus.CONFLICT.value());
        apiError.setMessage("El recurso ya existe: " + exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}
