/**
 * 
 */
package com.employee.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.entity.EmployeeEntity;
import com.employee.management.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/getAllEmployees")
	public List<EmployeeEntity> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/paginated")
	public Page<EmployeeEntity> getEmployeesPaginated(@RequestParam int page, @RequestParam int size,
			@RequestParam String sortBy, @RequestParam(defaultValue = "asc") String sortDirection) {
		return employeeService.getEmployeesPaginated(page, size, sortBy, sortDirection);
	}

	@PostMapping
	public EmployeeEntity addEmployee(@RequestBody EmployeeEntity employee) {
		return employeeService.addEmployee(employee);
	}

	@GetMapping("edit-employee/{id}")
	public EmployeeEntity getEmployee(@PathVariable Long id) {
		return employeeService.getEmployee(id);
	}

	@PutMapping("updateEmployee/{id}")
	public EmployeeEntity updateEmployee(@PathVariable Long id, @RequestBody EmployeeEntity employeeDetails) {
		return employeeService.updateEmployee(id, employeeDetails);
	}

	@DeleteMapping("deleteEmployee/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
	}

}
