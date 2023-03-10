package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person getByLogin(String login);

    Optional<Person> findByLogin(String login);

    List<Person> findAllByEnabled(boolean enabled);
}
