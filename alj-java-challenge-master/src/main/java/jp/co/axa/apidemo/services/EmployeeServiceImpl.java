package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeApiException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value="employee")
    @CacheEvict(allEntries = true, cacheNames = "employee")
    @Scheduled(fixedDelay=30000)
    public List<Employee> retrieveEmployees() {
        return employeeRepository.findAll();
    }

    @Cacheable(value="employee")
    @CacheEvict(allEntries = true, cacheNames = "employee")
    @Scheduled(fixedDelay=30000)
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.orElse(null);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @CacheEvict(value = "employee")
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @CachePut(value="employee")
    @CacheEvict(allEntries = true, cacheNames = "employee")
    @Scheduled(fixedDelay=30000)
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}