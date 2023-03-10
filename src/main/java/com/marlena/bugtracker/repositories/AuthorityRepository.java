package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("select a from Person p join p.authorities a where p.login = :login ORDER BY a.id ASC")
    Iterable<Authority> findAllByPersonLogin(String login);

    Optional<Authority> findByName(AuthorityName name);
}