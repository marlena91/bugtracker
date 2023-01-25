package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}