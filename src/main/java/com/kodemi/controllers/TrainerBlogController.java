package com.kodemi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.BlogDto;
import com.kodemi.model.Blog;
import com.kodemi.service.BlogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainer/blogs")
@RequiredArgsConstructor
public class TrainerBlogController {

	private final BlogService blogService;

	@PostMapping
	public String createBlog(@RequestBody Blog blog) {
		return blogService.createBlog(blog);
	}

	@GetMapping
	public List<BlogDto> getMyBlogs(@RequestParam String trainerId) {
		return blogService.getBlogsByTrainerId(trainerId);
	}

	@GetMapping("/{blogId}")
	public BlogDto getBlogById(@PathVariable String blogId) {
		return blogService.getBlogId(blogId);
	}

	@PutMapping("/{blogId}")
	public String updateBlog(@PathVariable String blogId, @RequestBody Blog blog) {
		return blogService.updateBlog(blogId, blog);
	}

	@DeleteMapping("/{blogId}")
	public String deleteBlog(@PathVariable String blogId) {
		return blogService.deleteBlog(blogId);
	}
}
