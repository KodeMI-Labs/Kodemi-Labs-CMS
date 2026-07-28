package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.BlogDto;
import com.ContentManagementSystem.CMS.model.Blog;
import com.ContentManagementSystem.CMS.service.BlogService;
import com.ContentManagementSystem.CMS.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class
BlogController {

    private final BlogService blogService;
    private final S3Service s3Service;
    @PostMapping(value = "/upload-thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, "blog-thumbnails");
        return Map.of("thumbnailUrl", url);
    }

    @PostMapping("/create-dto")
    public Blog createBlogDto(@RequestBody BlogDto blogDto) {
        return blogService.createBlog(blogDto);
    }

    @PostMapping("/create")
    public String createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog);
    }

    @GetMapping("/{blog_Id}")
    public BlogDto getBlogById(@PathVariable String blog_Id) {
        return blogService.getBlogId(blog_Id);
    }

    @GetMapping("/all")
    public List<BlogDto> getAllBlog() {
        return blogService.getAllBlog();
    }

    @PutMapping("/update/{blog_Id}")
    public String updateBlog(
            @PathVariable String blog_Id,
            @RequestBody Blog blog) {
        return blogService.updateBlog(blog_Id, blog);
    }

    @DeleteMapping("/delete/{blog_Id}")
    public String deleteBlog(@PathVariable String blog_Id) {
        return blogService.deleteBlog(blog_Id);
    }
}
