package com.med.backend.exception;

import com.med.backend.dto.auth.ApiError;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        ApiError apiError = new ApiError();
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setUrl(env.getExecutionStepInfo().getPath().toString());
        apiError.setMethod(env.getField().getName());

        if (ex instanceof AccessDeniedException) {
            apiError.setHttpCode(HttpStatus.FORBIDDEN.value());
            apiError.setMessage("Acceso denegado. No tienes los permisos necesarios para acceder a esta función.");
            apiError.setBackendMessage("Acceso denegado: " + ex.getMessage());
            logger.error("AccessDeniedException: {}", ex.getMessage());
        } else if (ex instanceof BadCredentialsException) {
            apiError.setHttpCode(HttpStatus.UNAUTHORIZED.value());
            apiError.setMessage("El nombre de usuario o la contraseña son incorrectos. Por favor, verifica tus credenciales.");
            apiError.setBackendMessage("Credenciales inválidas: " + ex.getMessage());
            logger.error("BadCredentialsException: {}", ex.getMessage());
        } else if (ex instanceof AuthenticationException || ex instanceof AuthenticationCredentialsNotFoundException || ex instanceof AuthenticationServiceException) {
            apiError.setHttpCode(HttpStatus.UNAUTHORIZED.value());
            apiError.setMessage("Acceso no autorizado. Se requiere un token válido.");
            apiError.setBackendMessage("Acceso no autorizado: " + ex.getMessage());
            logger.error("UnauthorizedAccessException: {}", ex.getMessage());
        } else if (ex instanceof DuplicateResourceException) {
            DuplicateResourceException duplicateEx = (DuplicateResourceException) ex;
            apiError.setHttpCode(HttpStatus.CONFLICT.value());
            apiError.setMessage(duplicateEx.getFrontendMessage());
            apiError.setBackendMessage("Recurso duplicado: " + duplicateEx.getMessage());
            logger.error("DuplicateResourceException: {}", duplicateEx.getMessage());
        } else if (ex instanceof org.springframework.dao.DuplicateKeyException) {
            String errorMessage = ex.getMessage();
            String fieldName = "dato";
            String duplicatedValue = "";
            // Intentar extraer el nombre del campo y el valor duplicado del mensaje
            if (errorMessage.contains("dup key")) {
                int startIndex = errorMessage.indexOf("dup key: { ") + 11;
                int endIndex = errorMessage.indexOf(" }", startIndex);
                if (startIndex != -1 && endIndex != -1) {
                    String fieldInfo = errorMessage.substring(startIndex, endIndex);
                    if (fieldInfo.contains(":")) {
                        String[] parts = fieldInfo.split(":");
                        fieldName = parts[0].trim();  // Nombre del campo
                        duplicatedValue = parts[1].trim();  // Valor duplicado
                    }
                }
            }
            apiError.setHttpCode(HttpStatus.CONFLICT.value());
            apiError.setMessage("El recurso con el campo '" + fieldName + "' y valor '" + duplicatedValue + "' ya existe. Verifica los datos enviados.");
            apiError.setBackendMessage("Clave duplicada: " + ex.getMessage());
            logger.error("DuplicateKeyException: {}", ex.getMessage());
        } else if (ex instanceof IllegalArgumentException) {
            apiError.setHttpCode(HttpStatus.BAD_REQUEST.value());
            apiError.setMessage("Datos de entrada inválidos. Por favor, verifica tu solicitud.");
            apiError.setBackendMessage("Entrada inválida: " + ex.getMessage());
            logger.error("IllegalArgumentException: {}", ex.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            apiError.setHttpCode(HttpStatus.BAD_REQUEST.value());
            apiError.setMessage("Error en los datos enviados. Por favor, revisa y corrige los campos.");
            apiError.setBackendMessage("Argumento no válido: " + ex.getMessage());
            logger.error("MethodArgumentNotValidException: {}", ex.getMessage());
        } else if (ex instanceof ObjectNotFoundException) {
            ObjectNotFoundException objectNotFoundException = (ObjectNotFoundException) ex;
            apiError.setHttpCode(HttpStatus.NOT_FOUND.value());
            // Usar el mensaje de la excepción directamente para el frontend
            apiError.setMessage(objectNotFoundException.getMessage());
            apiError.setBackendMessage("Recurso no encontrado: " + objectNotFoundException.getMessage());
            logger.error("ObjectNotFoundException: {}", objectNotFoundException.getMessage());
        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException validationException = (ConstraintViolationException) ex;
            String errors = validationException.getConstraintViolations().stream()
                    .map(violation -> {
                        // Extraer solo el último segmento del PropertyPath
                        String fieldName = violation.getPropertyPath().toString();
                        String simpleFieldName = fieldName.contains(".")
                                ? fieldName.substring(fieldName.lastIndexOf('.') + 1)
                                : fieldName;
                        return "Campo '" + simpleFieldName + "' " + violation.getMessage();
                    })
                    .collect(Collectors.joining(", "));

            apiError.setHttpCode(HttpStatus.BAD_REQUEST.value());
            apiError.setMessage("Error en los datos enviados: " + errors);
            apiError.setBackendMessage("Detalles de validación: " + errors);
            logger.error("ConstraintViolationException: {}", errors);
        } else {
            apiError.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            apiError.setMessage("Error interno en el servidor. Por favor, intenta más tarde.");
            apiError.setBackendMessage(ex.getMessage());
            logger.error("Unhandled Exception: {}", ex.getMessage(), ex);
        }

        return GraphqlErrorBuilder.newError(env)
                .message(apiError.getMessage()) // Mensaje amigable para el frontend
                .extensions(Map.of(
                        "code", resolveErrorCode(ex),
                        "status", apiError.getHttpCode(),
                        "detail", apiError
                ))
                .build();
    }

    private String resolveErrorCode(Throwable ex) {
        if (ex instanceof AccessDeniedException) return "ACCESS_DENIED";
        if (ex instanceof AuthenticationException || ex instanceof AuthenticationCredentialsNotFoundException || ex instanceof AuthenticationServiceException)
            return "UNAUTHORIZED_ACCESS";
        if (ex instanceof DuplicateResourceException) return "DUPLICATE_RESOURCE";
        if (ex instanceof MethodArgumentNotValidException) return "INVALID_ARGUMENT";
        if (ex instanceof ObjectNotFoundException) return "OBJECT_NOT_FOUND";
        return "INTERNAL_ERROR";
    }
}

