package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;

    private List<Employee> empList;

    private Employee emp;

    private Employee emp1;

    @Before
    public void setUp() throws Exception {
        this.employeeRepository = mock(EmployeeRepository.class);
        emp = new Employee(1, "Leif Weyand", 100000, "IT");
        emp1 = new Employee(2, "Leif Weyand", 100000, "IT");
        empList = Mockito.mock(List.class);
        empList.add(emp);
        empList.add(emp1);
    }

    @After
    public void tearDown() {
        empList.clear();
        emp = null;
        emp1 = null;
    }

    @Test
    public void retrieveEmployeesTest() throws Exception {
        //arrange
        when(employeeRepository.findAll()).thenReturn(empList);

        //act
        List<Employee> employeeList = employeeRepository.findAll();

        //assert
        assertNotNull(employeeList);
        assertEquals(empList, employeeList);

    }

    @Test
    public void retrieveEmployeesByIdTest() throws Exception {

        Long id = 1L;

        //arrange
        when(employeeRepository.findById(id)).thenReturn(Optional.ofNullable(emp));

        //act
        Employee employee = employeeRepository.findById(id).get();

        //assert
        assertEquals(emp, employee);
    }

}
