package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
