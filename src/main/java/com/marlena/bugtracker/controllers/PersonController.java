package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.services.AuthorityService;
import com.marlena.bugtracker.services.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class PersonController {
    
    private final PersonService userService;
    private final AuthorityService authorityService;

    @GetMapping
    public ModelAndView getAllUsers(){
        List<Person> users = userService.findAll();
        ModelAndView modelAndView = new ModelAndView("users/users");
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserById(userId);
        Iterable<Authority> authorities = authorityService.findAllByPersonLogin(Objects.requireNonNull(user.getBody()).getLogin());
        ModelAndView modelAndView = new ModelAndView("users/single");
        modelAndView.addObject("user", user.getBody());
        modelAndView.addObject("authority", authorities.iterator().next().getName());

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView displayNewUserForm() {
        ModelAndView modelAndView = new ModelAndView("users/new");
        modelAndView.addObject("user", new Person());
        modelAndView.addObject("authorities", authorityService.findAll());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView saveUser(@Valid @ModelAttribute("user") Person user, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("users/new");
            modelAndView.addObject("user", user);
            modelAndView.addObject("authorities", authorityService.findAll());
            return modelAndView;
        }
        userService.saveUserDetails(user);
        return getUserById(user.getId());
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        Person user = userService.findUserById(userId).getBody();
        assert user != null;
        Iterable<Authority> authorities = authorityService.findAllByPersonLogin(user.getLogin());
        user.setConfirmPwd(user.getPassword());
        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("user", user);
        modelAndView.addObject("authority", authorities.iterator().next().getName());
        return modelAndView;
    }

    @PostMapping("/{id}")
    public String updateUser(@Valid @ModelAttribute("user") Person user, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "users/edit.html";
        }
        userService.updateUser(user);
        return "redirect:/users/"+user.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value="id") Long userId) throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    @GetMapping("/authorities")
    public ModelAndView getAllUsersWithAuthorities() {
        List<Person> users = userService.findAll();
        ModelAndView modelAndView = new ModelAndView("users/authorities/authorities");
        modelAndView.addObject("users", users);
        for (Person user:
             users) {
            Set<Authority> newAuthorities = new HashSet<>();
            Authority generalAuthority = authorityService.findFirstByPersonLogin(user.getLogin());
            newAuthorities.add(generalAuthority);
            user.setAuthorities(newAuthorities);
        }
        return modelAndView;
    }
}

