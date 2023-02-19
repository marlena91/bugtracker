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

    public Iterable<Authority> findAllByPersonLogin(String login) {
        return authorityRepository.findAllByPersonLogin(login);
    }
//
//    public Authority findFirstByPersonLogin(String login) {
//        Iterable<Authority> authorities = authorityRepository.findAllByPersonLogin(login);
//        return authorities.iterator().next();
//    }
//
//    public ResponseEntity<Authority> findByAuthority(AuthorityName name) throws ResourceNotFoundException {
//        Authority authority = authorityRepository.findByName(name)
//                .orElseThrow(() -> new ResourceNotFoundException("Authority not found for this name :: "+ name));
//        return ResponseEntity.ok().body(authority);
//    }

    public Set<Authority> findAllAuthorities(){
        return findAll()
                .stream()
                .collect(Collectors.toSet());
    }


}
