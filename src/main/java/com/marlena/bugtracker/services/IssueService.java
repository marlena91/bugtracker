package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.IssueRepository;
import com.marlena.bugtracker.repositories.PersonRepository;
import com.marlena.bugtracker.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;

    public List<Issue> findAll(){
        return issueRepository.findAll();
    }

    public ResponseEntity<Issue> findById(Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        return ResponseEntity.ok().body(issue);
    }

    public boolean saveIssueDetails(Issue issue, Authentication authentication, Long projectId) {
        boolean isSaved = false;
        issue.setDateCreated(new Date());
        issue.setLastUpdated(new Date());
        Optional<Person> person = personRepository.findByLogin(authentication.getName());
        Optional<Project> project = projectRepository.findById(projectId);
        issue.setProject(project.get());
        issue.setCreator(person.get());
        Issue savedIssue = issueRepository.save(issue);
        if (null != savedIssue && savedIssue.getId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }
    public ResponseEntity<Issue> updateIssue(Issue issue) throws ResourceNotFoundException {
        Long id = issue.getId();
        Issue issueToUpdate = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issueToUpdate.setName(issue.getName());
        issueToUpdate.setDescription(issue.getDescription());
        issueToUpdate.setPriority(issue.getPriority());
        issueToUpdate.setStatus(issue.getStatus());
        issueToUpdate.setTags(issue.getTags());
        issueToUpdate.setType(issue.getType());
        issueToUpdate.setLastUpdated(new Date());
        final Issue updatedIssue = issueRepository.save(issueToUpdate);
        return ResponseEntity.ok(updatedIssue);
    }

    public Map<String, Boolean> deleteIssue(Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issueRepository.delete(issue);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
