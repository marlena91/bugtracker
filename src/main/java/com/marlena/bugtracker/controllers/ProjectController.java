package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.ProjectRepository;
import com.marlena.bugtracker.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final ProjectRepository projectRepository;

    @GetMapping
    public ModelAndView getAllProjects() {
        List<Project> projects = projectService.findAll();
        ModelAndView modelAndView = new ModelAndView("projects/index");
        modelAndView.addObject("projects", projects);

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
        ModelAndView modelAndView = new ModelAndView("projects/single");
        modelAndView.addObject("project", project.getBody());

        return modelAndView;
    }

    @PostMapping
    public String saveProject(@Valid @ModelAttribute("project") Project project, Errors errors) {
        if (errors.hasErrors()) {
            return "projects/new.html";
        }
        projectService.saveProjectDetails(project);
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


}
