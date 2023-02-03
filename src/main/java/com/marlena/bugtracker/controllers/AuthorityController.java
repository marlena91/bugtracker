package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.services.AuthorityService;
import com.marlena.bugtracker.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    private final PersonService userService;

    private final AuthorityService authorityService;

    @GetMapping("/{login}")
    public ModelAndView getAuthorities(@PathVariable(value = "login") String login) {
        Iterable<Authority> authorities = authorityService.findAllByPersonLogin(login);
        ModelAndView modelAndView = new ModelAndView("users/authorities/single");
        modelAndView.addObject("authority", authorities.iterator().next());
        modelAndView.addObject("login", login);
        return modelAndView;
    }

    @PostMapping("/{login}")
    public String updateAuthority(@PathVariable(value="login") String login, @ModelAttribute Authority authority, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "users/authorities/single/"+login;
        }
        Authority authorityToAdd = authorityService.findByAuthority(authority.getName()).getBody();
        ResponseEntity<Person> updateUser = userService.updateUserAuthorities(authorityToAdd, login);
        return "redirect:/users/"+ Objects.requireNonNull(updateUser.getBody()).getId();
    }

}
