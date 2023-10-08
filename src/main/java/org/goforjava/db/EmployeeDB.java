package org.goforjava.db;

import org.goforjava.domain.Employee;
import org.goforjava.domain.Id;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDB implements DB<Employee> {

    private final Map<Id, Employee> employeeMap = new HashMap<>();

    @Override
    public List<Employee> findAll() {
        return employeeMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Employee> findById(Id id) {

        var employee = employeeMap.get(id);

        if(employee!=null){
            return Optional.of(employee);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public void put(Id id, Employee toPut) {
        employeeMap.put(id, toPut);
    }
}
