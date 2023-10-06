package org.goforjava.domain;

import org.goforjava.db.DB;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        var thisYear = LocalDate.now().getYear();
        return employeeDB
                .findAll()
                .stream()
                .filter(employee ->  {
                    var bornYear = employee.getBirthDate().getYear();
                    var age = thisYear - bornYear;
                    return age >= years;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findThreeTopCompensatedEmployees() {
        return employeeDB
                .findAll()
                .stream()
                .sorted((e1, e2) -> e2.getGrossSalary().compareTo(e1.getGrossSalary()))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Department> findDepartmentWithLowestCompensationAverage() {
        return Optional.empty();

    }

    @Override
    public List<Employee> findEmployeesBasedIn(Location location) {
        return employeeDB.findAll().stream()
                .filter(employee -> {
                    var department = departmentDB.findById(employee.getDepartmentId());
                    Optional<Location> depLocation = department.map(Department::getLocation);
                    return depLocation.isPresent() && depLocation.get().equals(location);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Long> countEmployeesByHireYear() {
        Map<Integer, Long> result = new HashMap<>();
        employeeDB.findAll().forEach(employee -> {
            result.put(employee.getHireDate().getYear(), result.getOrDefault(employee.getHireDate().getYear(), 0L) + 1);
        });
        return result;
    }

    @Override
    public Map<Location, Long> countEmployeesByLocation() {
        Map<Location, Long> result = new HashMap<>();
        employeeDB.findAll().forEach(employee -> {
            var department = departmentDB.findById(employee.getDepartmentId());
            department.ifPresent(department1 -> {
                result.put(department1.getLocation(), result.getOrDefault(department1.getLocation(), 0L) + 1);
            });

        });

        return result;
    }
}
