package com.kodemi.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kodemi.dto.BlogDto;
import com.kodemi.model.Blog;
import com.kodemi.service.BlogService;
import com.kodemi.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final S3Service   s3Service;

    /** POST /blog/upload-thumbnail  (multipart/form-data, field: file) */
    @PostMapping(value = "/upload-thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, "blog-thumbnails");
        return Map.of("thumbnailUrl", url);
    }

    /** POST /blog/create */
    @PostMapping("/create")
    public String createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog);
    }

    /** POST /blog/create-dto */
    @PostMapping("/create-dto")
    public Blog createBlogDto(@RequestBody BlogDto blogDto) {
        return blogService.createBlog(blogDto);
    }

    /** GET /blog/all */
    @GetMapping("/all")
    public List<BlogDto> getAllBlog() {
        return blogService.getAllBlog();
    }

    /** GET /blog/{blog_Id} */
    @GetMapping("/{blog_Id}")
    public BlogDto getBlogById(@PathVariable String blog_Id) {
        return blogService.getBlogId(blog_Id);
    }

    /** DELETE /blog/delete/{blog_Id} */
    @DeleteMapping("/delete/{blog_Id}")
    public String deleteBlog(@PathVariable String blog_Id) {
        return blogService.deleteBlog(blog_Id);
    }
    
    /** GET /blog/verified - Fetch all verified/published blogs */
    @GetMapping("/verified")
    public List<BlogDto> getAllVerifiedBlogs() {
        return blogService.getBlogsByStatus("PUBLISHED");
    }

    /** GET /blog/trainer/{trainerId} - Fetch blogs by trainer ID */
    @GetMapping("/trainer/{trainerId}")
    public List<BlogDto> getBlogsByTrainerId(@PathVariable String trainerId) {
        return blogService.getBlogsByTrainerId(trainerId);
    }
}
