package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.request.EmployeeRequest;
import jp.co.axa.apidemo.exceptions.EmployeeApiException;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;

import static jp.co.axa.apidemo.validatorutils.ValidatorUtils.isNull;
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
        Employee emp = employeeService.getEmployee(employeeId);
        if (isNull(emp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not exist");
        }
        return emp;
    }

    /**
     * Create new employee
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@RequestBody @NotNull EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
            /**
             * validating employeeRequest object sent from request body
             */
            validateEmployee(employeeRequest);
            /**
             * Mapping employeeRequest to employee Entity
             * Persistent entities should not be used as arguments of @RequestMapping
             */
            if(employeeRequest.getId() > 0) {
                Employee emp = employeeService.getEmployee(employeeRequest.getId());
                if (!isNull(emp))
                    throw new EmployeeApiException("Employee Id is already exists");
                employee.setId(employeeRequest.getId());
            } else if ((employeeRequest.getId() == 0) || (employeeRequest.getId() < 0)) {
                throw new EmployeeApiException("Employee Id should be greater than 0 or non-negative");
            }
            employee.setName(employeeRequest.getName());
            employee.setSalary(employeeRequest.getSalary());
            employee.setDepartment(employeeRequest.getDepartment());

            /**
             * After Successful validation and mapping save the employee
             */
            employeeService.saveEmployee(employee);

        log.info("Employee Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(employee);
    }

    /**
     * Delete a employee by employeeId
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
            /**
             * To delete employee first check that employee
             *  already exists by retrieve employee
             *  if not exists it will throw custom API exception
             */
            Employee emp = employeeService.getEmployee(employeeId);

            /**
             * To delete employee by employeeId
             */
            if (!isNull(emp)) {
                employeeService.deleteEmployee(employeeId);
                log.info("Employee Deleted Successfully");
            }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("Deleted Successfully");
    }

    /**
     * Update a employee by employeeId
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody @NotNull EmployeeRequest employeeRequest,
                                                 @PathVariable(name = "employeeId") Long employeeId) {
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

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("Update Success");
    }
}
