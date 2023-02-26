package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.services.IssueService;
import com.marlena.bugtracker.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProjectService projectService;

    @MockBean
    IssueService issueService;

    @Test
    void getAllProjects() throws Exception {
        Project project = new Project();

        List<Project> projects = List.of(
                project
        );

        Page<Project> page = new PageImpl<>(projects);

        doReturn(page).when(projectService).findAll(any(), any());
        doReturn(Collections.emptySet()).when(projectService).findAllCreators();

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/projects"))
                .andExpect(model().attribute("projects", page))
                .andExpect(model().attribute("creators", Collections.emptySet()));
    }

}