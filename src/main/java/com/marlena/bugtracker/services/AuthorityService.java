package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.repositories.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public List<Authority> findAll(){
        return authorityRepository.findAll();
    }

}
