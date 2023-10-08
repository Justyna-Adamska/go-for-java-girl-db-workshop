package org.goforjava.domain;

import org.goforjava.db.DB;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class WorkshopEmployeeStatsService implements EmployeeStatsService{

    private final DB<Employee> employeeDB;
    private final DB<Department> departmentDB;

    public WorkshopEmployeeStatsService(DB<Employee> employeeDB, DB<Department> departmentDB) {
        this.employeeDB = employeeDB;
        this.departmentDB = departmentDB;
    }

    @Override
    public List<Employee> findEmployeesOlderThen(long years) {

        return employeeDB.findAll()
                .stream()
                .filter(employee -> LocalDateTime.now().getYear() -employee.getBirthDate().getYear() >= years).collect(Collectors.toList());
    }

    @Override
    public List<Employee> findThreeTopCompensatedEmployees() {

        return employeeDB
                .findAll()
                .stream()
                .sorted((employee1, employee2) -> employee2.getGrossSalary().compareTo(employee1.getGrossSalary()))
                .limit(3L)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Department> findDepartmentWithLowestCompensationAverage() {



        return Optional.empty();
    }

    @Override
    public List<Employee> findEmployeesBasedIn(Location location) {
        return List.of();
    }

    @Override
    public Map<Integer, Long> countEmployeesByHireYear() {
        return Map.of();
    }

    @Override
    public Map<Location, Long> countEmployeesByLocation() {
        return Map.of();
    }
}
