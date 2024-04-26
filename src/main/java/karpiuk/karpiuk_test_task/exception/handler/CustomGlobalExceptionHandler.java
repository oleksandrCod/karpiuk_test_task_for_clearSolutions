package karpiuk.karpiuk_test_task.exception.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.DuplicateEmailException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.InvalidBirthDateRangeException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.UserNotAdultException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERRORS = "errors";
    private static final String ERROR = "error";
    private static final String SPACE = " ";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(InvalidBirthDateRangeException.class)
    public ResponseEntity handleInvalidBirthDataRangeException(InvalidBirthDateRangeException ex,
                                                               WebRequest request) {

        log.warn(ex.getMessage());
        return createResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity handleInvalidBirthDataRangeException(DuplicateEmailException ex,
                                                               WebRequest request) {

        log.warn(ex.getMessage());
        return createResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAdultException.class)
    public ResponseEntity handleInvalidBirthDataRangeException(UserNotAdultException ex,
                                                               WebRequest request) {

        log.warn(ex.getMessage());
        return createResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleInvalidBirthDataRangeException(UserNotFoundException ex,
                                                               WebRequest request) {

        log.warn(ex.getMessage());
        return createResponse(ex, HttpStatus.NOT_FOUND);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + SPACE + message;

        }
        return e.getDefaultMessage();
    }

    private ResponseEntity<Object> createResponse(Exception ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, status);
        body.put(ERROR, ex.getMessage());
        return new ResponseEntity<>(body, status);
    }
}
