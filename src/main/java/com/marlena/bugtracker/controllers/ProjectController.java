package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.ProjectRepository;
import com.marlena.bugtracker.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
        return "projects/newProject.html";
    }

    @RequestMapping(value = "/saveProject", method = POST)
    public String saveProject(@ModelAttribute("project") Project project, Errors errors) {
        if (errors.hasErrors()) {
            return "projects/newProject.html";
        }
        projectService.saveProjectDetails(project);
        return "redirect:/projects/new";
    }

    @GetMapping("/{id}")
    public ModelAndView GetProjectById(@PathVariable(value = "id") Long projectId) throws ChangeSetPersister.NotFoundException {
        Project project = projectService.findProjectById(projectId);
        ModelAndView modelAndView = new ModelAndView("projects/singleProject");
        modelAndView.addObject("project", project);

        return modelAndView;
    }


}
