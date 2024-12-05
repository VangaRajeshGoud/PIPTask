package com.employee.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.employee.management.entity.EmployeeEntity;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.service.EmployeeService;

class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeService employeeService;

	public EmployeeEntity getEmployeeEntity() {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setEmail("test@gmil.com");
		employeeEntity.setFirstName("testFirstName");
		employeeEntity.setLastName("getLastName");
		employeeEntity.setDepartment("Employee");
		return employeeEntity;
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddEmployee() {
		EmployeeEntity employee = getEmployeeEntity();
		when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employee);
		EmployeeEntity savedEmployee = employeeService.addEmployee(employee);
		assertNotNull(savedEmployee);
		assertEquals("testFirstName", savedEmployee.getFirstName());
		assertEquals("getLastName", savedEmployee.getLastName());
		assertEquals("test@gmil.com", savedEmployee.getEmail());
		assertEquals("Employee", savedEmployee.getDepartment());
		verify(employeeRepository, times(1)).save(employee);
	}

	@Test
	void testGetEmployee() {
		EmployeeEntity employee = getEmployeeEntity();
		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
		EmployeeEntity fetchedEmployee = employeeService.getEmployee(1L);
		assertNotNull(fetchedEmployee);
		assertEquals("testFirstName", fetchedEmployee.getFirstName());
		assertEquals("getLastName", fetchedEmployee.getLastName());
		assertEquals("test@gmil.com", fetchedEmployee.getEmail());
		assertEquals("Employee", fetchedEmployee.getDepartment());
		verify(employeeRepository, times(1)).findById(1L);
	}

	@Test
	void testGetAllEmployees() {
		EmployeeEntity employee = getEmployeeEntity();
		when(employeeRepository.findAll()).thenReturn(List.of(employee));
		List<EmployeeEntity> employees = employeeService.getAllEmployees();
		assertNotNull(employees);
		assertEquals(1, employees.size());
		assertEquals("testFirstName", employees.get(0).getFirstName());
		verify(employeeRepository, times(1)).findAll();
	}

	@Test
	void testUpdateEmployee() {
		EmployeeEntity existingEmployee = getEmployeeEntity();
		EmployeeEntity updatedEmployee = new EmployeeEntity();
		updatedEmployee.setEmail("updated@gmil.com");
		updatedEmployee.setFirstName("updatedFirstName");
		updatedEmployee.setLastName("updatedLastName");
		updatedEmployee.setDepartment("HR");
		when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
		when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(updatedEmployee);
		EmployeeEntity result = employeeService.updateEmployee(1L, updatedEmployee);
		assertNotNull(result);
		assertEquals("updatedFirstName", result.getFirstName());
		assertEquals("updatedLastName", result.getLastName());
		assertEquals("updated@gmil.com", result.getEmail());
		assertEquals("HR", result.getDepartment());
		verify(employeeRepository, times(1)).findById(1L);
		verify(employeeRepository, times(1)).save(updatedEmployee);
	}

	@Test
	void testDeleteEmployee() {
		EmployeeEntity employee = getEmployeeEntity();
		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
		employeeService.deleteEmployee(1L);
		verify(employeeRepository, times(1)).deleteById(1L);
	}

	@Test
    void testEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(1L));
        verify(employeeRepository, times(1)).findById(1L);
    }
}
