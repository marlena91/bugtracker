package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    ModelAndView index() {
        List<Project> projects = projectService.findAll();
        ModelAndView modelAndView = new ModelAndView("projects/index");
        modelAndView.addObject("projects", projects);

        return modelAndView;
    }
}
