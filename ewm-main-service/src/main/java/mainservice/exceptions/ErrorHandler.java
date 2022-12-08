package mainservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNoSuchElementFoundException(NotFoundException ex) {
        log.info("NotFoundException: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("There are no object has requested.")
                .status(StatusError.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ApiError handleDataIntegrityViolationException(ConflictException ex) {
        log.info("ConflictException: {}", ex.getMessage());
        return ApiError.builder()
                .message(Objects.requireNonNull(ex.getMessage()).split(";")[0])
                .reason("Request contains invalid values.")
                .status(StatusError.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException ex) {
        log.info("NotValidException: {}", ex.getMessage());
        return ApiError.builder()
                .message("Validation has failed.")
                .reason("Request contains wrong data.")
                .status(StatusError.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError defaultHandle(final Exception ex) {
        log.info("500 {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Server side problem")
                .status(StatusError.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}
