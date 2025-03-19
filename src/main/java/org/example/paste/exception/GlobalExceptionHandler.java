package org.example.paste.exception;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.info("Validation error : {}", ex.getMessage());
        List<ValidationErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        new ValidationErrorResponse(
                                fieldError.getDefaultMessage(),
                                fieldError.getField()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PasteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePasteNotFoundException(PasteNotFoundException ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PasteTimeLimitExceeded.class)
    public ResponseEntity<ErrorResponse> handlePasteTimeLimitExceeded(PasteTimeLimitExceeded ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyInUse(UsernameAlreadyInUseException ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UserEmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleUserEmailAlreadyInUse(UserEmailAlreadyInUseException ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidPasteIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasteId(InvalidPasteIdException ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof UserNotFoundException) {
            return handleUserNotFound((UserNotFoundException) cause);
        }
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        logger.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        logger.error("Unexpected error {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
