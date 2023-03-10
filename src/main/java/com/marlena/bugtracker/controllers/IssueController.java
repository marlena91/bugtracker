package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.audit.AuditDataDTO;
import com.marlena.bugtracker.exceptions.ResourceNotFoundException;
import com.marlena.bugtracker.filters.IssueFilter;
import com.marlena.bugtracker.models.*;
import com.marlena.bugtracker.services.CommentService;
import com.marlena.bugtracker.services.IssueService;
import com.marlena.bugtracker.services.PersonService;
import com.marlena.bugtracker.services.UploadService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.Path;


@Controller
@RequestMapping("issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final PersonService userService;
    private final CommentService commentService;
    private final UploadService uploadService;
    private final EntityManager entityManager;

    @GetMapping
    public ModelAndView getAllIssues(@ModelAttribute IssueFilter filter,
                                     @RequestParam(value="deleted", required =false) String deleted) {
        List<Issue> issues = issueService.findAll(filter);
        ModelAndView modelAndView = new ModelAndView("issues/issues");
        modelAndView.addObject("issues", issues);
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("assignee", issueService.findAllAssigned());
        modelAndView.addObject("projects", issueService.findAllProjects());
        modelAndView.addObject("deleted", deleted);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getIssueById(@PathVariable(value = "id") Long id, HttpSession httpSession) throws ResourceNotFoundException {
        ResponseEntity<Issue> issue = issueService.findById(id);
        List<Comment> comments = commentService.findAllByIssueId(issue.getBody());

        ModelAndView modelAndView = new ModelAndView("issues/single");
        modelAndView.addObject("issue", issue.getBody());
        modelAndView.addObject("comment", new Comment());
        modelAndView.addObject("assignee", new Person());
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("users", userService.findAllEnabled());

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
        Map<String, Boolean> response = issueService.deleteIssue(id);
        if (response.get("deleted")) {
            return "redirect:/issues?deleted=true";
        }
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
    public ModelAndView addNewComment(@RequestParam("image") MultipartFile file, @Valid @ModelAttribute("comment") Comment comment,
                                      Errors errors, Authentication authentication, HttpSession httpSession)
            throws ResourceNotFoundException, IOException {
        Issue issue = (Issue) httpSession.getAttribute("issue");
        if (errors.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("issues/single");
            modelAndView.addObject("issue", issue);
            modelAndView.addObject("errors", errors);
            modelAndView.addObject("assignee", new Person());
            modelAndView.addObject("comments", commentService.findAllByIssueId(issue));
            return modelAndView;
        }
        commentService.saveCommentDetails(comment, authentication, httpSession);
        if(!file.isEmpty()){
            Path path = uploadService.uploadImage(file, comment.getId());
            commentService.savePathForImage(comment.getId(), path.getFileName());
        }


        return getIssueById(issue.getId(), httpSession);
    }

    @RequestMapping("/deleteComment")
    public String deleteComment(Model model, @RequestParam Long id, HttpSession httpSession) {
        Issue issue = (Issue) httpSession.getAttribute("issue");
        commentService.deleteById(id);
        return "redirect:/issues/"+issue.getId();
    }

    @GetMapping("/status")
    public String getAllStatus(Model model) {
        model.addAttribute("status", Status.values());
        model.addAttribute("issue", new Issue());
        return "/issues/component/statusSelect :: statusSelect";
    }

    @GetMapping("/status-updated")
    public String getStatusUpdated() {
        return "/issues/component/statusUpdated :: statusUpdated";
    }

    @PatchMapping("/status/{id}")
    ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Status status) {
        issueService.saveStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/priority")
    public String getAllPriority(Model model) {
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("issue", new Issue());
        return "/issues/component/prioritySelect :: prioritySelect";
    }

    @GetMapping("/priority-updated")
    public String getPriorityUpdated() {
        return "/issues/component/priorityUpdated :: priorityUpdated";
    }

    @PatchMapping("/priority/{id}")
    ResponseEntity<Void> updatePriority(@PathVariable Long id, @RequestBody Priority priority) {
        issueService.savePriority(id, priority);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/type")
    public String getAllType(Model model) {
        model.addAttribute("types", Type.values());
        model.addAttribute("issue", new Issue());
        return "/issues/component/typeSelect :: typeSelect";
    }

    @GetMapping("/type-updated")
    public String getTypeUpdated() {
        return "/issues/component/typeUpdated :: typeUpdated";
    }

    @PatchMapping("/type/{id}")
    ResponseEntity<Void> updateType(@PathVariable Long id, @RequestBody Type type) {
        issueService.saveType(id, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/{id}")
    ModelAndView history(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("issues/history");

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<AuditDataDTO> revisions = (List<AuditDataDTO>) auditReader.createQuery()
                .forRevisionsOfEntity(Issue.class, false, true)
                .add(AuditEntity.id().eq(id))
                .getResultList()
                .stream()
                .map(r -> new AuditDataDTO((Object[]) r))
                .collect(Collectors.toList());

        modelAndView.addObject("revisions", revisions);

        return modelAndView;
    }
}
