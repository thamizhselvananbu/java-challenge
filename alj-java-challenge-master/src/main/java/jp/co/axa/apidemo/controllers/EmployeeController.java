package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.request.EmployeeRequest;
import jp.co.axa.apidemo.exceptions.EmployeeApiException;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;

import static jp.co.axa.apidemo.validatorutils.ValidatorUtils.validateEmployee;

/**
 * Copyright (c) AXA. All Rights Reserved
 * Rest controller for Employee API endpoints
 */
@RestController
@RequestMapping("/api/v1")
@Log4j2
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieve all employees
     */
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    /**
     * Retrieve employee by employeeId
     * Added /employees/ into PATH values to differentiate
     * GET /employees full list and /employees by ID
     */
    @GetMapping(value = {"/employees/", "/employees/{employeeId}",})
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    /**
     * Create new employee
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@RequestBody @NotNull EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        try {
            /**
             * validating employeeRequest object sent from request body
             */
            validateEmployee(employeeRequest);

            /**
             * Mapping employeeRequest to employee Entity
             * Persistent entities should not be used as arguments of @RequestMapping
             */
            employee.setName(employeeRequest.getName());
            employee.setSalary(employeeRequest.getSalary());
            employee.setDepartment(employeeRequest.getDepartment());

            /**
             * After Successful validation and mapping save the employee
             */
            employeeService.saveEmployee(employee);
        } catch (EmployeeApiException e) {
            /**
             * Catch Employee API exceptions thrown during validations
             */
            log.info(e.getMessage());
            throw new EmployeeApiException(e.getMessage());
        } catch (Exception e) {
            /**
             * Catch Unexpected API exceptions and throw custom API Exception
             */
            log.debug(e.getCause());
            throw new EmployeeApiException("Internal error");
        }

        log.info("Employee Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    /**
     * Delete a employee by employeeId
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            /**
             * To delete employee first check that employee
             *  already exists by retrieve employee
             *  if not exists it will throw custom API exception
             */
            employeeService.getEmployee(employeeId);

            /**
             * To delete employee by employeeId
             */
            employeeService.deleteEmployee(employeeId);
        } catch (EmployeeApiException e) {
            /**
             * Catch Employee API exception if employee
             * user want to delete not exists
             */
            log.info("Employee deletion failed");
            throw new EmployeeApiException(e.getMessage());
        } catch (Exception e) {
            /**
             * Catch Unexpected API exception
             */
            log.debug(e.getCause());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
        log.info("Employee Deleted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
    }

    /**
     * Update a employee by employeeId
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody @NotNull EmployeeRequest employeeRequest,
                                                 @PathVariable(name = "employeeId") Long employeeId) {
        try {
            /**
             * Check that the employee user wants to
             *  update is exis or not
             *  if not exists Employee API exception will
             *  thrown from service layer
             */
            Employee emp = employeeService.getEmployee(employeeId);
            /**
             * Validate employeeRequest sent from requestbody
             */
            validateEmployee(employeeRequest);
            /**
             * Mapping employeeRequest to employee Entity
             * Persistent entities should not be used as arguments of @RequestMapping
             */
            emp.setName(employeeRequest.getName());
            emp.setSalary(employeeRequest.getSalary());
            emp.setDepartment(employeeRequest.getDepartment());
            /**
             * Update employee
             */
            employeeService.updateEmployee(emp);
        } catch (EmployeeApiException e) {
            /**
             * Catch Employee API exceptions thrown during validations
             */
            log.info("Update employee failed");
            throw new EmployeeApiException(e.getMessage());
        } catch (Exception e) {
            /**
             * Catch Unexpected API exception
             */
            log.debug(e.getCause());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Update Success");
    }
}
