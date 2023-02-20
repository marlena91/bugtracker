package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.AuthorityName;
import com.marlena.bugtracker.repositories.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public List<Authority> findAll(){
        return authorityRepository.findAll();
    }

    public Set<Authority> findAllAuthorities(){
        return findAll()
                .stream()
                .collect(Collectors.toSet());
    }


}
