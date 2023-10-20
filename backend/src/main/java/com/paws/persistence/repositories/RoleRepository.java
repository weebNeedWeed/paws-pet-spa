package com.paws.persistence.repositories;

import com.paws.persistence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findFirstByNameEquals(String name);
}
