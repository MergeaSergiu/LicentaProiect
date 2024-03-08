package com.spring.project.repository;

import com.spring.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepsitory extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}