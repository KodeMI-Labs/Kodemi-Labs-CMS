package com.kodemi.service;

import java.util.List;
import java.util.Map;

import com.kodemi.dto.BlogDto;
import com.kodemi.model.Blog;

public interface BlogService {

	// ── Trainer ────────────────────────────────────────────────────────────────
	Blog createBlog(BlogDto blogDto);

	String createBlog(Blog blog);

	String updateBlog(String blog_Id, Blog blog); // resets to DRAFT, clears reason

	String deleteBlog(String blog_Id);

	// ── Read ───────────────────────────────────────────────────────────────────
	BlogDto getBlogId(String blog_Id);

	List<BlogDto> getAllBlog();

	List<BlogDto> getBlogsByStatus(String status);

	List<BlogDto> getBlogsByTrainerId(String trainerId);

	// ── Admin ──────────────────────────────────────────────────────────────────
	String approveBlog(String blog_Id, String reviewedBy);

	String rejectBlog(String blog_Id, String reason, String reviewedBy);

	// ── Dashboard counts ───────────────────────────────────────────────────────
	Map<String, Long> getBlogStatusCounts();
	
	 
}
