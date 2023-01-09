package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.PersonRepository;
import com.marlena.bugtracker.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public boolean saveProjectDetails(Project project) {
        boolean isSaved = false;
        project.setDateCreated(new Date());
        Optional<Person> person = personRepository.findById(1L);
        project.setCreator(person.get());
        Project savedProject = projectRepository.save(project);
        System.out.println(savedProject);
        if(null != savedProject && savedProject.getId()>0){
            isSaved = true;
        }
        return isSaved;
    }
}
