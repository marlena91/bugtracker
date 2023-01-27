package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person getByLogin(String login);
}
