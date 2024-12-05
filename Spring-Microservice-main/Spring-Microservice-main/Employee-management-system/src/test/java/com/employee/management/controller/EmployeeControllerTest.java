package com.employee.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.employee.management.entity.EmployeeEntity;
import com.employee.management.entity.User;
import com.employee.management.repository.UserRepository;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;
	@MockBean
	private UserRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	private EmployeeEntity employee1, employee2;

	String dummyToken;

	@BeforeEach
	void setUp() {
		employee1 = getEmployeeEntity();
		employee2 = getEmployeeEntity();
		dummyToken = "Bearer " + JwtHelper.generateToken(getEmployeeEntity().getEmail());
		User user = new User();
		user.setUsername(getEmployeeEntity().getEmail());
		user.setPassword("test");
		when(repository.findByUsername(getEmployeeEntity().getEmail())).thenReturn(Optional.of(user));
	}

	@Test
	void testGetAllEmployees() throws Exception {
		List<EmployeeEntity> employees = Arrays.asList(employee1, employee2);
		when(employeeService.getAllEmployees()).thenReturn(employees);
		System.out.println(dummyToken);
		mockMvc.perform(get("/employees/getAllEmployees").header("Authorization", dummyToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value(getEmployeeEntity().getFirstName()))
				.andExpect(jsonPath("$[1].firstName").value(getEmployeeEntity().getFirstName()));
		verify(employeeService, times(1)).getAllEmployees();
	}

	@Test
	void testGetEmployeesPaginated() throws Exception {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName").ascending());
		Page<EmployeeEntity> page = new PageImpl<>(Arrays.asList(employee1, employee2), pageable, 2);
		when(employeeService.getEmployeesPaginated(0, 10, "firstName", "asc")).thenReturn(page);
		mockMvc.perform(get("/employees/paginated").param("page", "0").param("size", "10").param("sortBy", "firstName")
				.param("sortDirection", "asc").header("Authorization", dummyToken)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].firstName").value(getEmployeeEntity().getFirstName()))
				.andExpect(jsonPath("$.content[1].firstName").value(getEmployeeEntity().getFirstName()));
		verify(employeeService, times(1)).getEmployeesPaginated(0, 10, "firstName", "asc");
	}

	@Test
	void testAddEmployee() throws Exception {
		EmployeeEntity newEmployee = getEmployeeEntity();
		when(employeeService.addEmployee(any(EmployeeEntity.class))).thenReturn(employee1);
		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newEmployee)).header("Authorization", dummyToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value(getEmployeeEntity().getFirstName()))
				.andExpect(jsonPath("$.lastName").value(getEmployeeEntity().getLastName()));
		verify(employeeService, times(1)).addEmployee(any(EmployeeEntity.class));
	}

	@Test
    void testGetEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employee1);
        mockMvc.perform(get("/employees/edit-employee/{id}", 1L)
                .header("Authorization", dummyToken))  
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.firstName").value(getEmployeeEntity().getFirstName()))
                .andExpect(jsonPath("$.lastName").value(getEmployeeEntity().getLastName()));
        verify(employeeService, times(1)).getEmployee(1L);
    }

	@Test
	void testUpdateEmployee() throws Exception {
		EmployeeEntity updatedEmployee = getEmployeeEntity();
		when(employeeService.updateEmployee(eq(1L), any(EmployeeEntity.class))).thenReturn(updatedEmployee);
		mockMvc.perform(put("/employees/updateEmployee/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedEmployee)).header("Authorization", dummyToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$.email").value(getEmployeeEntity().getEmail()));
		verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeEntity.class));
	}

	@Test
	void testDeleteEmployee() throws Exception {
		doNothing().when(employeeService).deleteEmployee(1L);
		mockMvc.perform(delete("/employees/deleteEmployee/{id}", 1L).header("Authorization", dummyToken))
				.andExpect(status().isOk());
		verify(employeeService, times(1)).deleteEmployee(1L);
	}

	public EmployeeEntity getEmployeeEntity() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setEmail("test@gmil.com");
		employeeEntity.setFirstName("testFirstName");
		employeeEntity.setLastName("getLastName");
		employeeEntity.setDepartment("Employee");
		return employeeEntity;
	}
}
