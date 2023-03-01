package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.filters.ProjectFilter;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.PersonRepository;
import com.marlena.bugtracker.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final IssueService issueService;

    public Page<Project> findAll(ProjectFilter filter, Pageable pageable) {
        return projectRepository.findAll(filter.buildQuery(), pageable);
    }

    public List<Project> findAllEnabled() {
        return projectRepository.findAllByEnabled(true);
    }

    public List<Project> findAllByCreator(Person creator) {
        return projectRepository.findAllByCreatorAndEnabled(creator, true);
    }

    public Set<Person> findAllCreators(){
        return findAllEnabled()
                .stream()
                .map(Project::getCreator)
                .collect(Collectors.toSet());
    }

    public ResponseEntity<Project> findProjectById(Long id) throws ResourceNotFoundException {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + id));
        return ResponseEntity.ok().body(project);
    }

    public void saveProjectDetails(Project project, Authentication authentication) {
        project.setDateCreated(new Date());
        Optional<Person> person = personRepository.findByLogin(authentication.getName());
        project.setCreator(person.get());
        projectRepository.save(project);
    }

    public void updateProject(Project project) throws ResourceNotFoundException {
        Long id = project.getId();
        Project projectToUpdate = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + id));
        projectToUpdate.setName(project.getName());
        projectToUpdate.setDescription(project.getDescription());
        projectRepository.save(projectToUpdate);
    }

    public Map<String, Boolean> deleteProject(Long id) throws ResourceNotFoundException {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + id));
        List<Issue> projectIssues = issueService.findAllForProject(project);
        for(Issue issue : projectIssues) {
            issue.setEnabled(false);
            issueService.save(issue);
        }
        project.setEnabled(false);
        projectRepository.save(project);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }



}
