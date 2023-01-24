package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @Test
    void getAllProjects() throws Exception {
        Project project = new Project();
        project.setCode("CODE");

        List<Project> projects = List.of(
                project
        );

        doReturn(projects).when(projectService).findAll(any(), any());
        doReturn(Collections.emptySet()).when(projectService).findAllCreators();

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/index"))
                .andExpect(model().attribute("projects", projects))
                .andExpect(model().attribute("creators", Collections.emptySet()));
    }

}
