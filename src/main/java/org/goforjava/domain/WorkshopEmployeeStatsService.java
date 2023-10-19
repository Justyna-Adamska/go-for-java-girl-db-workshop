package org.goforjava.domain;

import org.goforjava.db.DB;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
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



        List<Employee> pracownicyDepartamenty =  employeeDB
                .findAll();

        Map<String, List<Employee>> pracownicyDepartamenty1 =  employeeDB
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(p -> (p.getId().getKey())/*Collectors.toMap(Employee::getDepartmentId,*/));

        Double lowestDeptSalary = Double.MAX_VALUE;
        Id lowestDepartment = new Id("");
        for (Map.Entry<String, List<Employee>> iterator : pracownicyDepartamenty1.entrySet()) {

            List<Employee> value = iterator.getValue();

            Double sum = 0.0;
            Double average = 0.0;


            for (int i = 0; i < value.size(); i++) {
                sum +=  value.get(i).getGrossSalary();

            }

            average = sum / value.size();

            if(average<lowestDeptSalary){
                lowestDeptSalary=average;
                lowestDepartment = value.get(0).getDepartmentId();
            }
        }

        return  departmentDB.findById(lowestDepartment);
    }



    @Override
    public List<Employee> findEmployeesBasedIn(Location location) {

        List<Department> departamentyWLokalizacji = departmentDB
                .findAll()
                .stream()
                .filter(department -> department.getLocation().equals(location))
                .collect(Collectors.toList());

        List<Employee> pracownicyLokalizacji = new ArrayList<>();


        for (int i = 0; i < departamentyWLokalizacji.size(); i++) {
            String department1 =  departamentyWLokalizacji.get(i).getId().getKey();
            pracownicyLokalizacji.addAll(employeeDB
                    .findAll()
                    .stream()
                    .filter(employee -> employee.getDepartmentId().getKey().equals(department1))
                    .collect(Collectors.toList()));

        }

        return pracownicyLokalizacji;
    }

    @Override
    public Map<Integer, Long> countEmployeesByHireYear() {

        Map<Integer,Long> pracownicyZatrudnieniWLatach = new HashMap<>();

        Map<Integer, List<Employee>> hireYears =  employeeDB
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(p -> (p.getHireDate().getYear())));


        for(var iterator : hireYears.entrySet()){
            pracownicyZatrudnieniWLatach.put(iterator.getKey(),new Long(iterator.getValue().size()));
        }


        return pracownicyZatrudnieniWLatach;
    }

    @Override
    public Map<Location, Long> countEmployeesByLocation() {

        Map<Location,Long> pracownicyPerLokalizacja = new HashMap<>();

        Map<Location, List<Department>> departamentyWLokalizacji = departmentDB
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(department -> department.getLocation()));


        for(var iterator : departamentyWLokalizacji.entrySet()){
            pracownicyPerLokalizacja.put(iterator.getKey(),new Long(findEmployeesBasedIn(iterator.getKey()).size()));
        }

        return pracownicyPerLokalizacja;
    }
}
