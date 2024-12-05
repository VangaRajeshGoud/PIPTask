package com.employee.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.employee.management.entity.EmployeeEntity;
import com.employee.management.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<EmployeeEntity> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Page<EmployeeEntity> getEmployeesPaginated(int page, int size, String sortBy, String sortDirection) {
		Sort sort = Sort.by(Sort.Order.by(sortBy));
		if ("desc".equalsIgnoreCase(sortDirection)) {
			sort = Sort.by(Sort.Order.desc(sortBy));
		}
		return employeeRepository.findAll(PageRequest.of(page, size, sort));
	}

	public EmployeeEntity addEmployee(EmployeeEntity employee) {
		return employeeRepository.save(employee);
	}

	public EmployeeEntity updateEmployee(Long id, EmployeeEntity employeeDetails) {
		EmployeeEntity employee = employeeRepository.findById(id).orElseThrow();
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmail(employeeDetails.getEmail());
		employee.setDepartment(employeeDetails.getDepartment());
		return employeeRepository.save(employee);
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

	public EmployeeEntity getEmployee(Long id) {
		Optional<EmployeeEntity> result = employeeRepository.findById(id);
		return result.get();

	}
}
