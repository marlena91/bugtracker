package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.filters.ProjectFilter;
import com.marlena.bugtracker.services.IssueService;
import com.marlena.bugtracker.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final IssueService issueService;

    @GetMapping
    public ModelAndView getAllProjects(@ModelAttribute ProjectFilter filter, Pageable pageable) {
        Page<Project> projects = projectService.findAll(filter, pageable);
        ModelAndView modelAndView = new ModelAndView("projects/projects");
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("creators", projectService.findAllCreators());

        return modelAndView;
    }

    @GetMapping("/new")
    public String displayNewProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/new.html";
    }

    @GetMapping("/{id}")
    public ModelAndView getProjectById(@PathVariable(value = "id") Long projectId) throws ResourceNotFoundException {
        ResponseEntity<Project> project = projectService.findProjectById(projectId);
        List<Issue> issues = issueService.findAllForProject(project.getBody());
        ModelAndView modelAndView = new ModelAndView("projects/single");
        modelAndView.addObject("project", project.getBody());
        modelAndView.addObject("issues", issues);
        return modelAndView;
    }

    @PostMapping
    public String saveProject(@Valid @ModelAttribute("project") Project project, Errors errors, Authentication authentication) {
        if (errors.hasErrors()) {
            return "projects/new.html";
        }
        projectService.saveProjectDetails(project, authentication);
        return "redirect:/projects/"+project.getId();
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProjectById(@PathVariable(value = "id") Long projectId) throws ResourceNotFoundException {
        ResponseEntity<Project> project = projectService.findProjectById(projectId);
        ModelAndView modelAndView = new ModelAndView("projects/edit");
        modelAndView.addObject("project", project.getBody());

        return modelAndView;
    }

    @PostMapping("/{id}")
    public String updateProject(@Valid @ModelAttribute("project") Project project, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "projects/edit.html";
        }
        projectService.updateProject(project);
        return "redirect:/projects/"+project.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable(value="id") Long projectId) throws ResourceNotFoundException {
        projectService.deleteProject(projectId);
        return "redirect:/projects";
    }
}
