package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.ProjectRepository;
import com.marlena.bugtracker.services.AuthorityService;
import com.marlena.bugtracker.services.PersonService;
import com.marlena.bugtracker.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("my-profile")
@RequiredArgsConstructor
public class ProfileController {
    private final PersonService userService;
    private final AuthorityService authorityService;

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;


    @GetMapping
    public String displayUserProfile(Model model, Authentication authentication) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserByLogin(authentication.getName());
        Authority generalAuthority = authorityService.findFirstByPersonLogin(user.getBody().getLogin());
        model.addAttribute("user", user.getBody());
        model.addAttribute("role", generalAuthority);
        return "users/profile/profile.html";
    }

    @GetMapping("/{login}")
    public ModelAndView displayUserData(@PathVariable(value = "login") String login) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserByLogin(login);
        ModelAndView modelAndView = new ModelAndView("users/profile/data");
        modelAndView.addObject("user", user.getBody());
        return modelAndView;
    }

    @GetMapping("/projects/{id}")
    public ModelAndView displayUserProjects(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Person creator = userService.findUserById(id).getBody();
        List<Project> projects = projectService.findAllByCreator(creator);
        ModelAndView modelAndView = new ModelAndView("users/profile/projects");
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("user", creator);
        return modelAndView;
    }

    @GetMapping("/issues/{id}")
    public ModelAndView displayUserIssues(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        ResponseEntity<Person> user = userService.findUserById(id);
        ModelAndView modelAndView = new ModelAndView("users/profile/issues");
        modelAndView.addObject("user", user.getBody());
        return modelAndView;
    }
}
