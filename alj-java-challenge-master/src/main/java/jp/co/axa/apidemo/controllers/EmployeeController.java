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
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieve all employees
     *
     */
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    /**
     * Retrieve employee by employeeId
     *
     */
    @GetMapping(value = {"/employees/", "/employees/{employeeId}",})
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    /**
     * Create new employee
     *
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        try {
            validateEmployee(employeeRequest);
            if(employeeRequest.getId() > 0) {
                employee.setId(employeeRequest.getId());
            } else {
                log.info("Employee creation is failed");
                throw new EmployeeApiException("EmployeeID should be greater than 0");
            }
            employee.setName(employeeRequest.getName());
            employee.setSalary(employeeRequest.getSalary());
            employee.setDepartment(employeeRequest.getDepartment());

            employeeService.saveEmployee(employee);
            log.info("Employee Saved Successfully");
        } catch (EmployeeApiException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            log.info("Internal Error");
            throw new EmployeeApiException("Internal error");
        }

       return ResponseEntity.status(HttpStatus.CREATED).body(employee);

    }

    /**
     * Delete a employee by employeeId
     *
     * @return
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            Employee emp = employeeService.getEmployee(employeeId);
            if (!isNull(emp)) {
                employeeService.deleteEmployee(employeeId);
            }
        } catch (EmployeeApiException e) {
            log.info("Employee deletion failed");
        } catch (Exception e) {
            log.debug(e.getCause());
            log.info("Employee deletion failed");
        }
        log.info("Employee Deleted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
    }

    /**
     * Update a employee by employeeId
     *
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody EmployeeRequest employeeRequest,
                               @PathVariable(name = "employeeId") Long employeeId) {
        try {
            Employee emp = employeeService.getEmployee(employeeId);
            if (emp != null) {
                validateEmployee(employeeRequest);
                if(employeeRequest.getId() > 0) {
                    emp.setId(employeeRequest.getId());
                } else {
                    log.info("Updating employee is failed");
                    throw new EmployeeApiException("EmployeeID should be greater than 0");
                }
                emp.setName(employeeRequest.getName());
                emp.setSalary(employeeRequest.getSalary());
                emp.setDepartment(employeeRequest.getDepartment());
            }
        } catch (EmployeeApiException e) {
            log.info("Update employee failed");
        } catch (Exception e) {
            log.info("Internal error");
            throw new EmployeeApiException("Internal error");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Update Success");
    }

}
