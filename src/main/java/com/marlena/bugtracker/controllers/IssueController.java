package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.filters.IssueFilter;
import com.marlena.bugtracker.models.Comment;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Status;
import com.marlena.bugtracker.services.CommentService;
import com.marlena.bugtracker.services.IssueService;
import com.marlena.bugtracker.services.PersonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final PersonService userService;
    private final CommentService commentService;

    @GetMapping
    public ModelAndView getAllIssues(@ModelAttribute IssueFilter filter) {
        List<Issue> issues = issueService.findAll(filter);
        ModelAndView modelAndView = new ModelAndView("issues/issues");
        modelAndView.addObject("issues", issues);
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("assignee", issueService.findAllAssigned());
        modelAndView.addObject("projects", issueService.findAllProjects());

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getIssueById(@PathVariable(value = "id") Long id, HttpSession httpSession) throws ResourceNotFoundException {
        ResponseEntity<Issue> issue = issueService.findById(id);
        List<Comment> comments = commentService.findAllByIssueId(issue.getBody());
        Collections.reverse(comments);
        ModelAndView modelAndView = new ModelAndView("issues/single");
        modelAndView.addObject("issue", issue.getBody());
        modelAndView.addObject("comment", new Comment());
        modelAndView.addObject("assignee", new Person());
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("users", userService.findAll());
        httpSession.setAttribute("issue", issue.getBody());
        return modelAndView;
    }

    @GetMapping("/new/{projectId}")
    public ModelAndView displayNewIssueForm(@PathVariable Long projectId) {
        ModelAndView modelAndView = new ModelAndView("issues/new");
        modelAndView.addObject("issue", new Issue());
        return modelAndView;
    }

    @PostMapping("/new/{projectId}")
    public String saveIssue(@Valid @ModelAttribute("issue") Issue issue, Errors errors, @PathVariable Long projectId, Authentication authentication) {
        if (errors.hasErrors()) {
            return "issues/new.html";
        }
        issueService.saveIssueDetails(issue, authentication, projectId);
        return "redirect:/issues/" + issue.getId();
    }

    @GetMapping("edit/{id}")
    public ModelAndView editIssueById(@PathVariable Long id) throws ResourceNotFoundException {
        ResponseEntity<Issue> issue = issueService.findById(id);
        ModelAndView modelAndView = new ModelAndView("issues/edit");
        modelAndView.addObject("issue", issue.getBody());
        return modelAndView;
    }

    @PostMapping("/{id}")
    public String updateIssue(@Valid @ModelAttribute("issue") Issue issue, Errors errors) throws ResourceNotFoundException {
        if (errors.hasErrors()) {
            return "issues/edit.html";
        }
        issueService.updateIssue(issue);
        return "redirect:/issues/" + issue.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteIssue(@PathVariable Long id) throws ResourceNotFoundException {
        ResponseEntity<Issue> issue = issueService.findById(id);
        List<Comment> comments = commentService.findAllByIssueId(issue.getBody());
        for(Comment comment : comments) {
            commentService.deleteById(comment.getId());
        }
        issueService.deleteIssue(id);
        return "redirect:/issues";
    }

    @PostMapping("newAssignee")
    public String updateAssignee(@ModelAttribute("assignee") Person assignee, @RequestParam Long issueId) throws ResourceNotFoundException {
        issueService.updateAssignee(assignee, issueId);
        return "redirect:/issues/"+issueId;
    }

    @GetMapping("/deleteAssignee")
    public String deleteAssignee(@RequestParam Long issueId) throws ResourceNotFoundException {
        issueService.deleteAssignee(issueId);
        return "redirect:/issues/" + issueId;
    }

    @PostMapping("/addNewComment")
    public String addNewComment(Model model, @ModelAttribute("comment") Comment comment,Authentication authentication, HttpSession httpSession){
        Issue issue = (Issue) httpSession.getAttribute("issue");
        commentService.saveCommentDetails(comment, authentication, httpSession);
        return "redirect:/issues/"+ issue.getId();
    }

    @RequestMapping("/deleteComment")
    public String deleteComment(Model model, @RequestParam Long id, HttpSession httpSession) {
        Issue issue = (Issue) httpSession.getAttribute("issue");
        commentService.deleteById(id);
        return "redirect:/issues/"+issue.getId();
    }

    @PatchMapping("/status/{id}")
    ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Status status) {
        issueService.saveStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
