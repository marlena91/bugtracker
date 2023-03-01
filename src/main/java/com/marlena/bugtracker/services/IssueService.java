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

    public List<Issue> findAll(IssueFilter filter) {
        return issueRepository.findAll(filter.buildQuery());
    }

    public List<Issue> findAllEnabled() {
        return issueRepository.findAllByEnabled(true);
    }

    public List<Issue> findAllForProject(Project project) {
        return issueRepository.findAllByProject(project);
    }

    public List<Issue> findAllEnabledAndProject(Project project) {
        return issueRepository.findAllByEnabledAndProject(true, project);
    }

    public Set<Person> findAllAssigned() {
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

    public void saveIssueDetails(Issue issue, Authentication authentication, Long projectId) {
        issue.setCreatedDate(new Date());
        Optional<Person> person = personRepository.findByLogin(authentication.getName());
        Optional<Project> project = projectRepository.findById(projectId);
        issue.setProject(project.get());
        issue.setCreator(person.get());
        issueRepository.save(issue);
    }
    public void updateIssue(Issue issue) throws ResourceNotFoundException {
        Long id = issue.getId();
        Issue issueToUpdate = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issueToUpdate.setName(issue.getName());
        issueToUpdate.setDescription(issue.getDescription());
        issueToUpdate.setPriority(issue.getPriority());
        issueToUpdate.setStatus(issue.getStatus());
        issueToUpdate.setTags(issue.getTags());
        issueToUpdate.setType(issue.getType());
        if ((issueToUpdate.getStatus() == Status.DONE) && (issueToUpdate.getAssignee() != null)) {
            Mail mail = new Mail();
            mail.setRecipient(issueToUpdate.getAssignee().getEmail());
            mail.setSubject(issueToUpdate.getName());
            mailService.sendMail(mail);
        }
        issueToUpdate.setLastModifiedDate(new Date());
        issueRepository.save(issueToUpdate);
    }

    public Map<String, Boolean> deleteIssue(Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issue.setEnabled(false);
        issueRepository.save(issue);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public void updateAssignee(Person assignee, Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issue.setAssignee(assignee);
        issueRepository.save(issue);
    }

    public void deleteAssignee(Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        issue.setAssignee(null);
        issueRepository.save(issue);
    }

    public void saveStatus(Long id, Status status) {
        Issue issue = issueRepository.getReferenceById(id);
        issue.setStatus(status);
        issue.setLastModifiedDate(new Date());
        if((issue.getStatus() == Status.DONE) && (issue.getAssignee()!=null)){
            Mail mail = new Mail();
            mail.setRecipient(issue.getAssignee().getEmail());
            mail.setSubject(issue.getName());
            mailService.sendMail(mail);
        }
        issueRepository.save(issue);
    }

    public void savePriority(Long id, Priority priority) {
        Issue issue = issueRepository.getReferenceById(id);
        issue.setPriority(priority);
        issue.setLastModifiedDate(new Date());
        issueRepository.save(issue);
    }

    public void saveType(Long id, Type type) {
        Issue issue = issueRepository.getReferenceById(id);
        issue.setType(type);
        issue.setLastModifiedDate(new Date());
        issueRepository.save(issue);
    }

    public void save(Issue issue) {
        issueRepository.save(issue);
    }

    public List<Issue> findAllByAssignee(Person user) {
        return issueRepository.findAllByAssignee(user);
    }
}
