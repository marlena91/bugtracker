package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.IssueRepository;
import com.marlena.bugtracker.services.IssueService;
import com.marlena.bugtracker.services.ProjectService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService  issueService;
    private final ProjectService projectService;

    @GetMapping
    public ModelAndView getAllIssues(){
        List<Issue> issues = issueService.findAll();
        ModelAndView modelAndView = new ModelAndView("issues/issues");
        modelAndView.addObject("issues", issues);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getIssueById(@PathVariable(value="id") Long id) throws ResourceNotFoundException {
        ResponseEntity<Issue> issue = issueService.findById(id);
        ModelAndView modelAndView = new ModelAndView("issues/single");
        modelAndView.addObject("issue", issue.getBody());
        return modelAndView;
    }


    @GetMapping("/new/{projectId}")
    public ModelAndView displayNewIssueForm(@PathVariable Long projectId) throws ResourceNotFoundException {
        ModelAndView modelAndView = new ModelAndView("issues/new");
        modelAndView.addObject("issue", new Issue());
        modelAndView.addObject("project", projectId);
        return modelAndView;
    }

    @PostMapping("/{projectId}")
    public String saveIssue(@Valid @ModelAttribute("issue") Issue issue, @PathVariable Long projectId, Errors errors, Authentication authentication) {
        if (errors.hasErrors()) {
            return "projects/new.html";
        }
        issueService.saveIssueDetails(issue, authentication, projectId);
        return "redirect:/issues/"+issue.getId();
    }



    //update



    //delete



}
