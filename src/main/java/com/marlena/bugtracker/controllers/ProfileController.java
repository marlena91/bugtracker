package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.*;
import com.marlena.bugtracker.repositories.IssueRepository;
import com.marlena.bugtracker.repositories.PersonRepository;
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
    public ModelAndView displayProfileData(HttpSession httpSession) {
        ProfileData profileData = getLoggedInPersonData((Person) httpSession.getAttribute("loggedInPerson"));
        ModelAndView modelAndView = new ModelAndView("users/profile/data");
        modelAndView.addObject("profileData", profileData);
        return modelAndView;
    }

    @GetMapping("/edit/data")
    public ModelAndView editProfileData(HttpSession httpSession) {
        ProfileData profileData = getLoggedInPersonData((Person) httpSession.getAttribute("loggedInPerson"));
        ModelAndView modelAndView = new ModelAndView("users/profile/edit");
        modelAndView.addObject("profileData", profileData);
        return modelAndView;
    }

    private ProfileData getLoggedInPersonData(Person user){
        ProfileData profileData = new ProfileData();
        profileData.setUserRealName(user.getUserRealName());
        profileData.setEmail(user.getEmail());
        return profileData;
    }

    @PostMapping(value="/updateData")
    public String updateData(@Valid @ModelAttribute("profileData") ProfileData profileData, Errors errors, HttpSession httpSession) {
        if(errors.hasErrors()){
            return "users/profile/edit";
        }
        Person user = (Person) httpSession.getAttribute("loggedInPerson");
        user.setUserRealName(profileData.getUserRealName());
        user.setEmail(profileData.getEmail());
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
