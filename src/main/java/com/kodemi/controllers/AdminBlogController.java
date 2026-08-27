package com.kodemi.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.BlogDto;
import com.kodemi.service.BlogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

	private final BlogService blogService;
	
	@GetMapping("all")
	public List<BlogDto> getBlogs(@RequestParam(required = false, defaultValue = "DRAFT") String status) {
	    if (status != null && !status.isBlank()) {
	        return blogService.getBlogsByStatus(status.toUpperCase());
	    }
	    return blogService.getAllBlog();
	}

	/*@GetMapping
	public List<BlogDto> getBlogs(@RequestParam(required = false) String status) {
		if (status != null && !status.isBlank()) {
			return blogService.getBlogsByStatus(status.toUpperCase());
		}
		return blogService.getAllBlog();
	}*///these are  the admin endpoints weare not using.

	@GetMapping("/{blogId}")
	public BlogDto getBlogById(@PathVariable String blogId) {
		return blogService.getBlogId(blogId);
	}

	@PutMapping("/{blogId}/approve")
	public String approveBlog(@PathVariable String blogId, @RequestBody Map<String, String> body) {
		String reviewedBy = body.getOrDefault("reviewedBy", "ADMIN");
		return blogService.approveBlog(blogId, reviewedBy);
	}

	@PutMapping("/{blogId}/reject")
	public String rejectBlog(@PathVariable String blogId, @RequestBody Map<String, String> body) {
		String reason = body.getOrDefault("reason", "");
		String reviewedBy = body.getOrDefault("reviewedBy", "ADMIN");
		return blogService.rejectBlog(blogId, reason, reviewedBy);
	}

	@GetMapping("/stats/counts")
	public Map<String, Long> getBlogCounts() {
		return blogService.getBlogStatusCounts();
	}
}
