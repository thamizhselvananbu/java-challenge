package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeApiException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value="employee")
    public List<Employee> retrieveEmployees() {
        return employeeRepository.findAll();
    }

    @Cacheable(value="employee")
    public Employee getEmployee(Long employeeId) {
        return  employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EmployeeApiException("Employee Id is not exist"));

    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @CacheEvict(value = "employee")
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @CachePut(value="employee")
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}