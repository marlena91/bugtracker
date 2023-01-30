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

import java.util.ArrayList;
import java.util.List;

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
        ModelAndView modelAndView = new ModelAndView("users/single");
        modelAndView.addObject("user", user.getBody());

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
        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("user", user.getBody());

        return modelAndView;
    }

    @GetMapping("/authorities")
    public ModelAndView getAuthorities(@RequestParam String login) {
        Iterable<Authority> authorities = authorityService.findAllByPersonLogin(login);
        List<String> namesOfAuthorities = new ArrayList<>();
        for (Authority authority:
             authorities) {
            namesOfAuthorities.add(authority.getName());
        }
        ModelAndView modelAndView = new ModelAndView("users/authorities");
        modelAndView.addObject("authorities", namesOfAuthorities);

        return modelAndView;
    }
}

