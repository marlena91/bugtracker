package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.repositories.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public Iterable<Authority> findAllByPersonLogin(String login) {
        return authorityRepository.findAllByPersonLogin(login);
    }

    public ResponseEntity<Authority> findByAuthority(String name) throws ResourceNotFoundException {
        Authority authority = authorityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Authority not found for this name :: "+ name));
        return ResponseEntity.ok().body(authority);
    }
}
