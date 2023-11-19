package com.paws.repositories;

import com.paws.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select e from Employee e where e.username = :username")
    Employee getEmployeeByUsername(String username);

    @Query("select e from Employee e where e.id = :id")
    Employee findById(long id);

    @Query("select e from Employee e join fetch e.roles r where r.name in :roleNames")
    List<Employee> getAllEmployees(Set<String> roleNames);
}
