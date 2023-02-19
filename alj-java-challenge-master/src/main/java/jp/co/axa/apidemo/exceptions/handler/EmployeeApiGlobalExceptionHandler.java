package jp.co.axa.apidemo.exceptions.handler;

import jp.co.axa.apidemo.exceptions.EmployeeApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.ws.Response;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class EmployeeApiGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP = "timestamp";

    private static final String STATUS = "status";

    private static final String ERROR = "error";

    private static final String MESSAGE = "message";

    @ExceptionHandler(value = {EmployeeApiException.class})
    public EmployeeApiException handleEmployeeApiExceptions(EmployeeApiException apiException, WebRequest request) {
        return apiException;
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseStatusException handleEmployeeApiExceptionStatus(ResponseStatusException apiException, WebRequest request) {
        return new ResponseStatusException(apiException.getStatus(), apiException.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleApiExceptions(EmployeeApiException apiException, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(ERROR, "Internal server error");
        body.put(MESSAGE,"Something went wrong");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, "Invalid argument type");
        body.put(MESSAGE,"Argument type mismatched");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, "Required value is missing");
        body.put(MESSAGE,"Required value is not present in request URL");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
