package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Comment;
import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.repositories.CommentRepository;
import com.marlena.bugtracker.repositories.PersonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;


    public List<Comment> findAll(){
        return commentRepository.findAll();
    }
    public List<Comment> findAllByIssueId(Issue issue){
        return commentRepository.findAllByIssue(issue);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public boolean saveCommentDetails(Comment comment, Authentication authentication, HttpSession httpSession) {
        boolean isSaved = false;
        comment.setDateCreated(new Date());
        Optional<Person> person = personRepository.findByLogin(authentication.getName());
        Issue issue = (Issue) httpSession.getAttribute("issue");
        comment.setIssue(issue);
        comment.setAuthor(person.get());
        Comment savedComment = commentRepository.save(comment);
        if (null != savedComment && savedComment.getId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }
}
