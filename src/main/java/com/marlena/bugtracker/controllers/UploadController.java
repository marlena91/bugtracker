package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.services.CommentService;
import com.marlena.bugtracker.services.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Controller
@AllArgsConstructor
public class UploadController {

    private final UploadService uploadService;
    private final CommentService commentService;

    @PostMapping("/upload")
    public String uploadImage(Model model,
                              @RequestParam("image") MultipartFile file,
                              @RequestParam("commentId") Long commentId,
                              @RequestParam("issueId") Long issueId) throws IOException {

        Path path = uploadService.uploadImage(file, commentId);
        commentService.savePathForImage(commentId, path.getFileName());

        return "redirect:/issues/"+issueId;
    }
}