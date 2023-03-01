package com.marlena.bugtracker.repositories;

import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    List<Issue> findAllByEnabled(Boolean enabled);

    List<Issue> findAllByProject(Project project);

    List<Issue> findAllByAssignee(Person person);
    List<Issue> findAllByAssigneeAndEnabled(Person person, Boolean enabled);

    List<Issue> findAllByEnabledAndProject(Boolean enabled, Project project);

}
