package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.UserData;
import com.marlena.bugtracker.services.AuthorityService;
import com.marlena.bugtracker.services.PersonService;
import com.marlena.bugtracker.services.UserDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    private final UserDataService userDataService;

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
        UserData userData = userDataService.getUserDataForEditUser(user);
        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("userData", userData);
        modelAndView.addObject("authorities", user.getAuthorities());

        return modelAndView;
    }

    @PostMapping("/{id}")
    public String updateUser(@Valid @ModelAttribute("userData") UserData userData, Errors errors, @PathVariable Long id) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "users/edit.html";
        }
        Person user = userService.findUserById(id).getBody();
        userService.updateUser(userData, id);
        assert user != null;
        return "redirect:/users/"+user.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value="id") Long userId) throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

//    @GetMapping("/authorities")
//    public ModelAndView getAllUsersWithAuthorities() {
//        List<Person> users = userService.findAll();
//        ModelAndView modelAndView = new ModelAndView("users/authorities/authorities");
//        modelAndView.addObject("users", users);
//        for (Person user:
//             users) {
//            Set<Authority> newAuthorities = new HashSet<>();
//            Authority generalAuthority = authorityService.findFirstByPersonLogin(user.getLogin());
//            newAuthorities.add(generalAuthority);
//            user.setAuthorities(newAuthorities);
//        }
//        return modelAndView;
//    }

    @GetMapping("edit/password/{id}")
    public ModelAndView editPasswordByUserId(@PathVariable(value="id") Long userId) throws ResourceNotFoundException {
        Person user = userService.findUserById(userId).getBody();
        ModelAndView modelAndView = new ModelAndView("users/profile/password");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("edit/password/{id}")
    public String updatePassword(@Valid @ModelAttribute("user") Person user, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "users/profile/password.html";
        }
        userService.updateUserPwd(user);
        return "redirect:/my-profile";

    }
}

