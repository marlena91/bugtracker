package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.services.AuthorityService;
import com.marlena.bugtracker.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/users/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    private final PersonService userService;
    private final AuthorityService authorityService;

    @GetMapping()
    public ModelAndView getAllUsersWithAuthorities() {
        List<Person> users = userService.findAll();
        ModelAndView modelAndView = new ModelAndView("users/authorities/authorities");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/{login}")
    public ModelAndView getAuthorities(Model model, @PathVariable(value = "login") String login) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserByLogin(login);
        ModelAndView modelAndView = new ModelAndView("users/authorities/single");
        modelAndView.addObject("user", user.getBody());
        modelAndView.addObject("authorities", authorityService.findAll());
        return modelAndView;
    }

    @PostMapping()
    public String updateAuthority(Model model, @ModelAttribute("user") Person user, @ModelAttribute Authority authority, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "users/authorities/single/"+user.getLogin();
        }
        userService.updateUserAuthorities(user);
        return "redirect:/users/authorities";
    }

}
