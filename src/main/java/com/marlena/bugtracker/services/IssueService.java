package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.*;
import com.marlena.bugtracker.filters.IssueFilter;
import com.marlena.bugtracker.repositories.IssueRepository;
import com.marlena.bugtracker.repositories.PersonRepository;
import com.marlena.bugtracker.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;
    private final MailService mailService;

    public List<Issue> findAll(IssueFilter filter){
        return issueRepository.findAll(filter.buildQuery());
    }
    public List<Issue> findAllEnabled() {
        return issueRepository.findAllByEnabled(true);
    }

    public List<Issue> findAllForProject(Project project) {
        return issueRepository.findAllByProject(project);
    }

    public Set<Person> findAllCreators(){
        return findAllEnabled()
                .stream()
                .map(Issue::getCreator)
                .collect(Collectors.toSet());
    }

    public Set<Person> findAllAssigned(){
        return findAllEnabled()
                .stream()
                .map(Issue::getAssignee)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<Project> findAllProjects() {
        return findAllEnabled()
                .stream()
                .map(Issue::getProject)
                .collect(Collectors.toSet());
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
        if((issueToUpdate.getStatus() == Status.DONE) && (issueToUpdate.getAssignee()!=null)){
            Mail mail = new Mail();
            mail.setRecipient(issueToUpdate.getAssignee().getEmail());
            mail.setSubject(issueToUpdate.getName());
            mailService.sendMail(mail);
        }
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

    public Map<String, Boolean> updateAssignee(Person assignee, Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issue.setAssignee(assignee);
        issueRepository.save(issue);
        Map<String, Boolean> response = new HashMap<>();
        response.put("updated", Boolean.TRUE);
        return response;
    }

    public Map<String, Boolean> deleteAssignee(Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issue.setAssignee(null);
        issueRepository.save(issue);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deletedAssignee", Boolean.TRUE);
        return response;
    }

    public void saveStatus(Long id, Status status) {
        Issue issue = issueRepository.getReferenceById(id);
        issue.setStatus(status);
        issueRepository.save(issue);
    }
}
