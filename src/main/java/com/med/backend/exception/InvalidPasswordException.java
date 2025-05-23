package com.med.backend.exception;

/**
 * Excepción lanzada cuando se proporciona una contraseña inválida.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructor que crea una excepción con un mensaje predeterminado.
     */
    public InvalidPasswordException() {
        super("Invalid password"); // Mensaje predeterminado
    }

    /**
     * Constructor que permite especificar un mensaje de error.
     *
     * @param message El mensaje de error.
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Constructor que permite especificar un mensaje de error y una causa.
     *
     * @param message El mensaje de error.
     * @param cause La causa de la excepción.
     */
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
