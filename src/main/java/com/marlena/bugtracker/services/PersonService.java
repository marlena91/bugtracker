package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.repositories.AuthorityRepository;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final AuthorityRepository authorityRepository;

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public ResponseEntity<Person> findUserById(Long id) throws ResourceNotFoundException {
        Person user = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        return ResponseEntity.ok().body(user);
    }

    public boolean saveUserDetails(Person user) {
        boolean isSaved = false;
        Optional<Authority> authority = authorityRepository.findById(1L);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority.get());
        user.setAuthorities(authorities);
        Person savedUser = personRepository.save(user);
        System.out.println(savedUser);
        if (null != savedUser && savedUser.getId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }


}
