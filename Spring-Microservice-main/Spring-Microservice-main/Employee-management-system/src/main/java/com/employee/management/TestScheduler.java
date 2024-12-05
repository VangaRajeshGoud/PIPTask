package com.employee.management;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.employee.management.entity.EmployeeEntity;
import com.employee.management.repository.EmployeeRepository;

@Component
public class TestScheduler {

	@Autowired
	EmployeeRepository employeeRepository;

	private volatile Integer count = 0;

	// Time in milliseconds
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		addEmployee();
		// System.out.println("Current time is: " + System.currentTimeMillis());
	}

	// Runs 5 seconds after the last execution finishes
	@Scheduled(fixedDelay = 5000)
	public void reportAfterDelay() {
		// System.out.println("Task executed with fixed delay");
	}

	@Scheduled(cron = "0 0 2 * * ?")
	public void reportAtSpecificTime() {
		// System.out.println("Scheduled task executed at 2 AM");
	}

	public void addEmployee() {
		count++;
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setEmail("test@gmil.com" + count);
		employeeEntity.setFirstName(getRandomName());
		employeeEntity.setLastName(getRandomName());
		employeeEntity.setDepartment("Employee");
		employeeRepository.save(employeeEntity);

	}

	private static final String[] FIRST_NAMES = { "Chandana", "Ravi", "Sita", "Venkatesh", "Naveen", "Anjali",
			"Lakshmi", "Sai", "Pranitha", "Harsha", "Nithya", "Rajesh", "Kavya", "Srinivas", "Pooja", "Krishna", "Teja",
			"Shankar", "Manju", "Aditi", "Surya", "Divya", "Sandeep", "Geetha", "Sreeja" };

	public static String getRandomName() {
		Random random = new Random();
		int index = random.nextInt(FIRST_NAMES.length);
		return FIRST_NAMES[index];
	}
}
