package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.AuthorityRepository;
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
    private final AuthorityRepository authorityRepository;

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
    public String displayNewUserForm(Model model) {
        model.addAttribute("user", new Person());
        return "users/new";
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") Person user, Errors errors) {
        if (errors.hasErrors()) {
            return "users/new.html";
        }
        userService.saveUserDetails(user);
        return "redirect:/users/"+user.getId();
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserById(userId);
        Iterable<Authority> authorities = authorityService.findAllByPersonLogin(Objects.requireNonNull(user.getBody()).getLogin());
        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("user", user.getBody());
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

    @GetMapping("/authorities/{login}")
    public ModelAndView getAuthorities(@PathVariable(value = "login") String login) {
        Iterable<Authority> authorities = authorityService.findAllByPersonLogin(login);
        ModelAndView modelAndView = new ModelAndView("users/authorities");
        modelAndView.addObject("authority", authorities.iterator().next());
        modelAndView.addObject("login", login);

        return modelAndView;
    }

    @PostMapping("/authorities/{login}")
    public String updateAuthority(@PathVariable(value="login") String login, @ModelAttribute Authority authority, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "users/authorities/"+login;
        }
        Authority authorityToAdd = authorityService.findByAuthority(authority.getName()).getBody();
        ResponseEntity<Person> updateUser = userService.updateUserAuthorities(authorityToAdd, login);
        return "redirect:/users/"+ Objects.requireNonNull(updateUser.getBody()).getId();
    }

}

