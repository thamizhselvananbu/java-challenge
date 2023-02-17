package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

/**
 * Copyright (c) AXA. All Rights Reserved
 * Service Interface for EmployeeAPI
 */
public interface EmployeeService {

    /**
     * retrieve all employee list
     * Return: List<Employee>
     */
    List<Employee> retrieveEmployees();

    /**
     * retrieve a employee by its id
     * Params: employeeId
     * Return: Employee
     */
    Employee getEmployee(Long employeeId);

    /**
     * Create new employee
     * Params: Employee
     */
    void saveEmployee(Employee employee);

    /**
     * Delete a employee
     * Params: employeeId
     */
    void deleteEmployee(Long employeeId);

    /**
     * Update a employee
     * Params: Employee
     */
    void updateEmployee(Employee employee);
}