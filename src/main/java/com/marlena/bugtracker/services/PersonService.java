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
    public List<Person> findAllEnabled(){
        return personRepository.findAllByEnabled(true);
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

    public void saveUserDetails(Person user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        personRepository.save(user);
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

    public void updateUserAuthorities(Person user) throws ResourceNotFoundException {
        Person userToUpdate = findUserById(user.getId()).getBody();
        assert userToUpdate != null;
        userToUpdate.setAuthorities(user.getAuthorities());
        final Person updatedUser = personRepository.save(userToUpdate);
        ResponseEntity.ok().body(updatedUser);
    }

    public Map<String, Boolean> deleteUser(Long id) throws ResourceNotFoundException {
        Person user = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pot found for this id :: " + id));
        user.setEnabled(false);
        personRepository.save(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public void prepareAdminUser() {
        if(personRepository.findByLogin("admin").isEmpty()){
            Person admin = new Person();
            admin.setLogin("admin");
            admin.setPassword("password");
            admin.setUserRealName("Admin");
            admin.setEmail("admin@test.com");
            List<Authority> authorities = authorityService.findAll();
            admin.setAuthorities(new HashSet<>(authorities));
            saveUserDetails(admin);
        }
    }


}
