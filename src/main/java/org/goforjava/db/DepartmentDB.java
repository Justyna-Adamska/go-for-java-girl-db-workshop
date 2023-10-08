package org.goforjava.db;

import org.goforjava.domain.Department;
import org.goforjava.domain.Id;

import java.util.*;
import java.util.stream.Collectors;

public class DepartmentDB implements DB<Department> {

    private final Map<Id, Department> departmentMap = new HashMap<>();

    @Override
    public List<Department> findAll() {
        return  departmentMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Department> findById(Id id) {

        var department = departmentMap.get(id);
        if(department!=null){
            return Optional.of(department);
        }
        else{
        return Optional.empty();}
    }

    @Override
    public void put(Id id, Department toPut) {
        departmentMap.put(id, toPut);
    }
}
