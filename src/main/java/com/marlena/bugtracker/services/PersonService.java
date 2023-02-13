package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.UserData;
import com.marlena.bugtracker.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public ResponseEntity<Person> findUserById(Long id) throws ResourceNotFoundException {
        Person user = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<Person> findUserByLogin(String login) throws ResourceNotFoundException {
        Person user = personRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + login));
        return ResponseEntity.ok().body(user);
    }

    public boolean saveUserDetails(Person user) {
        boolean isSaved = false;
        Set<Authority> userSetAuthorities = getFullAuthorities(user.getAuthorities().iterator().next());
        user.setAuthorities(userSetAuthorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Person savedUser = personRepository.save(user);
        if (null != savedUser && savedUser.getId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }

    public void updateUser(UserData userData, Long id) throws ResourceNotFoundException {
        Person userToUpdate = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this login :: " + id));
        userToUpdate.setUserRealName(userData.getUserRealName());
        userToUpdate.setLogin(userData.getLogin());
        userToUpdate.setEmail(userData.getEmail());
        final Person updatedUser = personRepository.save(userToUpdate);
        ResponseEntity.ok(updatedUser);
    }

    public void updateUserPwd(Person user) throws ResourceNotFoundException {
        Person userToUpdate = personRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + user.getId()));
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        final Person updatedUser = personRepository.save(userToUpdate);
        ResponseEntity.ok(updatedUser);
    }

    public ResponseEntity<Person> updateUserAuthorities(Authority authority, String login) throws ResourceNotFoundException {
        Person userToUpdate = personRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + login));

        Set<Authority> userSetAuthorities = getFullAuthorities(authority);
        userToUpdate.setAuthorities(userSetAuthorities);
        final Person updatedUser = personRepository.save(userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    public Map<String, Boolean> deleteUser(Long id) throws ResourceNotFoundException {
        Person user = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pot found for this id :: " + id));
        personRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    private Set<Authority> getFullAuthorities(Authority authority) {
        Set<Authority> enabledAuthorities = authorityService.findAllAuthorities();
        Set<Authority> userSetAuthorities = new HashSet<>();
        for (Authority auth:
                enabledAuthorities) {
            if(auth.getId() >= authority.getId()){
                userSetAuthorities.add(auth);
            }
        }
        return userSetAuthorities;
    }
}
