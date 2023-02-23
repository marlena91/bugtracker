package com.marlena.bugtracker.controllers;

import com.marlena.bugtracker.services.CommentService;
import com.marlena.bugtracker.services.IssueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@AllArgsConstructor
public class UploadController {

    private final IssueService issueService;
    private final CommentService commentService;
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @PostMapping("/upload")
    public String uploadImage(Model model,
                              @RequestParam("image") MultipartFile file,
                              @RequestParam("commentId") Long commentId,
                              @RequestParam("issueId") Long issueId) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        commentService.savePathForImage(commentId, fileNameAndPath.getFileName());

        return "redirect:/issues/"+issueId;
    }
}