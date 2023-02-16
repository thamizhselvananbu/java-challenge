package jp.co.axa.apidemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jp.co.axa.apidemo.entities.request.EmployeeRequest;
import jp.co.axa.apidemo.exceptions.EmployeeApiException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class EmployeeControllerTest {

    private EmployeeController employeeController;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private EmployeeRequest employee;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.employeeController = mock(EmployeeController.class);
        employee = new EmployeeRequest();
    }

    @After
    public void tearDown() {
        employee = null;
    }

    @Test
    @WithMockUser()
    public void getEmployeesTest() throws Exception {

        mockMvc.perform(get("/api/v1/employees"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    public void getEmployeesByIdTest() throws Exception {

        mockMvc.perform(get("/api/v1/employees/1"))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value("Leif Weyand"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    public void saveEmployeesTest() throws Exception {

        employee = new EmployeeRequest(1, "Leif Weyand", 100000, "IT");

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Leif Weyand"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(status().isCreated()).andReturn().getResponse();

        int id = JsonPath.parse(response.getContentAsString()).read("$.id");
        Long id1 = new Long(String.valueOf(id));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Leif Weyand"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(status().isOk());

    }

    @Test
    public void updateEmployeeTest() throws Exception {

        employee = new EmployeeRequest(1, "Leif Weyand", 100000, "IT");

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Leif Weyand"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(status().isCreated()).andReturn().getResponse();

        int id = JsonPath.parse(response.getContentAsString()).read("$.id");
        Long id1 = new Long(String.valueOf(id));

        EmployeeRequest updatedEmployee = new EmployeeRequest(1, "Leif Weyand", 20000, "IT");

        mockMvc.perform(put("/api/v1/employees/{employeeId}", id1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test(expected = EmployeeApiException.class)
    @WithMockUser()
    public void saveEmployeeWithInvalidIdTest() throws Exception {

        employee = new EmployeeRequest(-1, "Leif Weyand", 100000, "IT");

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        int id = JsonPath.parse(response.getContentAsString()).read("$.id");
        Long id1 = new Long(String.valueOf(id));

        assertEquals(0, employeeService.getEmployee(id1).getId());
        assertNull(employeeService.getEmployee(id1).getName());
        assertEquals(0, employeeService.getEmployee(id1).getSalary());
        assertNull(employeeService.getEmployee(id1).getDepartment());

    }
    @Test
    @WithMockUser()
    public void deleteEmployeeTest() throws Exception {

        employee = new EmployeeRequest(1, "Leif Weyand", 100000, "IT");

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Leif Weyand"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(status().isCreated()).andReturn().getResponse();

        int id = JsonPath.parse(response.getContentAsString()).read("$.id");
        Long id1 = new Long(String.valueOf(id));

        assertNotNull(employeeService.getEmployee(id1));
        assertEquals("Leif Weyand", employeeService.getEmployee(id1).getName());
        assertEquals(100000, employeeService.getEmployee(id1).getSalary());
        assertEquals("IT", employeeService.getEmployee(id1).getDepartment());

        mockMvc.perform(delete("/api/v1/employees/{employeeId}", id1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
