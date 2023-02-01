package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person getByLogin(String login);

    Optional<Person> findByLogin(String login);
}
