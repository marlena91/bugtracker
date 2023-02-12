package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.*;
import com.marlena.bugtracker.repositories.IssueRepository;
import com.marlena.bugtracker.repositories.PersonRepository;
import com.marlena.bugtracker.repositories.ProjectRepository;
import com.marlena.bugtracker.services.AuthorityService;
import com.marlena.bugtracker.services.PersonService;
import com.marlena.bugtracker.services.ProjectService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("my-profile")
@RequiredArgsConstructor
public class ProfileController {
    private final PersonService userService;
    private final AuthorityService authorityService;

    private final ProjectService projectService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final IssueRepository issueRepository;


    @GetMapping
    public String displayUserProfile(Model model, Authentication authentication, HttpSession httpSession) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserByLogin(authentication.getName());
        Authority generalAuthority = authorityService.findFirstByPersonLogin(authentication.getName());
        model.addAttribute("user", user.getBody());
        model.addAttribute("role", generalAuthority);
        httpSession.setAttribute("loggedInPerson", user.getBody());
        return "users/profile/profile.html";
    }

    @GetMapping("/data")
    public ModelAndView displayUserData(HttpSession httpSession) {
        UserData userData = getLoggedInPersonData((Person) httpSession.getAttribute("loggedInPerson"));
        ModelAndView modelAndView = new ModelAndView("users/profile/data");
        modelAndView.addObject("userData", userData);
        return modelAndView;
    }

    @GetMapping("/edit/data")
    public ModelAndView editUserData(HttpSession httpSession) {
        UserData userData = getLoggedInPersonData((Person) httpSession.getAttribute("loggedInPerson"));
        ModelAndView modelAndView = new ModelAndView("users/profile/edit");
        modelAndView.addObject("userData", userData);
        return modelAndView;
    }

    private UserData getLoggedInPersonData(Person user){
        UserData userData = new UserData();
        userData.setUserRealName(user.getUserRealName());
        userData.setEmail(user.getEmail());
        return userData;
    }

    @PostMapping(value="/updateData")
    public String updateData(@Valid @ModelAttribute("userData") UserData userData, Errors errors, HttpSession httpSession) {
        if(errors.hasErrors()){
            return "users/profile/edit";
        }
        Person user = (Person) httpSession.getAttribute("loggedInPerson");
        user.setUserRealName(userData.getUserRealName());
        user.setEmail(userData.getEmail());
        personRepository.save(user);
        httpSession.setAttribute("loggedInPerson", user);
        return "redirect:/my-profile";
    }

    @GetMapping("/edit/password")
    public ModelAndView editUserPassword() {
        UserPassword userPassword = new UserPassword();
        ModelAndView modelAndView = new ModelAndView("users/profile/password");
        modelAndView.addObject("userPassword", userPassword);
        return modelAndView;
    }

    @PostMapping(value="/updatePassword")
    public String updatePassword(@Valid @ModelAttribute("userPassword") UserPassword userPassword, Errors errors, HttpSession httpSession) {
        if(errors.hasErrors()){
            return "users/profile/password";
        }
        Person user = (Person) httpSession.getAttribute("loggedInPerson");
        user.setPassword(passwordEncoder.encode(userPassword.getPassword()));
        personRepository.save(user);
        httpSession.setAttribute("loggedInPerson", user);
        return "redirect:/my-profile";
    }


    @GetMapping("/projects")
    public ModelAndView displayUserProjects(HttpSession httpSession) {
        Person creator = (Person) httpSession.getAttribute("loggedInPerson");
        List<Project> projects = projectService.findAllByCreator(creator);
        ModelAndView modelAndView = new ModelAndView("users/profile/projects");
        modelAndView.addObject("projects", projects);
        return modelAndView;
    }

    @GetMapping("/issues")
    public ModelAndView displayUserIssues(HttpSession httpSession) {
        Person user = (Person) httpSession.getAttribute("loggedInPerson");
        List<Issue> issues = issueRepository.findAllByAssignee(user);
        ModelAndView modelAndView = new ModelAndView("users/profile/issues");
        modelAndView.addObject("issues", issues);
        return modelAndView;
    }

}
