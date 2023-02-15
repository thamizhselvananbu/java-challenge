package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.request.EmployeeRequest;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest
public class EmployeeControllerTest {

    private EmployeeController employeeController;

    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    private EmployeeRequest employee;

    @Before
    public void setUp() {
        this.employeeController = mock(EmployeeController.class);
        this.employeeService = mock(EmployeeService.class);
        employee = new EmployeeRequest();
    }

    @After
    public void tearDown() {
        employee = null;
    }

    @Test
    public void testGetEmployees() throws Exception {

        mockMvc.perform(get("/api/v1/employees/1"))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value("Leif Weyand"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(status().isOk());


    }
}
