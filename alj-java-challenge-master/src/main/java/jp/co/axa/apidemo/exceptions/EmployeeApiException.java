package jp.co.axa.apidemo.exceptions;

import lombok.Getter;

/*Custom Employee Api Exception class to
* display error messages in proper format
* with message and error code
* */
@Getter
public class EmployeeApiException extends RuntimeException {

    public EmployeeApiException(final String errorMessage) {
        super(errorMessage);

    }
}
