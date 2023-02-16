package jp.co.axa.apidemo.validatorutils;

import jp.co.axa.apidemo.entities.request.EmployeeRequest;
import jp.co.axa.apidemo.exceptions.EmployeeApiException;

/**
 * Copyright (c) AXA. All Rights Reserved
 * User: Thamizh
 * Date: 2023/02/13
 * Time: 10:16
 */
public final class ValidatorUtils {

    /**
     * Private Constructor for utility classes
     */
    private ValidatorUtils() {
        //nothing to initialize
    }

    /**
     * Checks if the parameter is not <Code>null</Code>
     *
     * @param object the object to test
     * @return true, if the object is not null
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }

    public static void validateEmployee(EmployeeRequest employeeRequest) {
            if (isNull(employeeRequest.getName())
                    || employeeRequest.getSalary() <= 0
                    || isNull(employeeRequest.getDepartment())
            ) {
                throw new EmployeeApiException("Invalid Employee json");
            } else {
            throw new EmployeeApiException("Employee should not be null");
        }
    }
}
