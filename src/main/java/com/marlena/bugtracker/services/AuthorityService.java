package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.repositories.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public Iterable<Authority> findAllByPersonLogin(String login) {
        return authorityRepository.findAllByPersonLogin(login);
    }
}
