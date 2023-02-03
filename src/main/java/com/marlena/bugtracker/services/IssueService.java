package com.marlena.bugtracker.services;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.repositories.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    public List<Issue> findAll(){
        return issueRepository.findAll();
    }

    public ResponseEntity<Issue> findById(Long id) throws ResourceNotFoundException {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + id));
        return ResponseEntity.ok().body(issue);
    }
}
