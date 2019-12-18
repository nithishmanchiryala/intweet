package com.intuit.intweet.dao.repository;

import com.intuit.intweet.dao.entity.EmployeesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeesEntity, Integer> {

    EmployeesEntity findByEmployeeId(String employeeId);
}
