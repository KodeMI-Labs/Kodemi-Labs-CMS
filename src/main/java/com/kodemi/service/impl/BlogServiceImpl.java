package com.kodemi.service.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.BlogDto;
import com.kodemi.model.Blog;
import com.kodemi.repository.BlogRepository;
import com.kodemi.service.BlogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    // ── Helpers ────────────────────────────────────────────────────────────────

    private Blog findOrThrow(String blog_Id) {
        Blog blog = blogRepository.findById(blog_Id);
        if (blog == null) throw new ResourceNotFoundException("Blog not found with id: " + blog_Id);
        return blog;
    }

    private BlogDto toDto(Blog blog) {
        BlogDto dto = new BlogDto();
        BeanUtils.copyProperties(blog, dto);
        // Fallback thumbnail
        if (dto.getThumbnail() == null
                && blog.getFeatured_images() != null
                && !blog.getFeatured_images().isEmpty()) {
            dto.setThumbnail(blog.getFeatured_images().get(0));
        }
        return dto;
    }

    // ── Trainer ────────────────────────────────────────────────────────────────

    @Override
    public Blog createBlog(BlogDto blogDto) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDto, blog);
        blog.setBlog_Id(UUID.randomUUID().toString());
        blog.setStatus("DRAFT");                     // always starts as DRAFT
        blog.setRejection_reason(null);
        blog.setCreated_at(LocalDateTime.now());
        blog.setUpdated_at(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    @Override
    public String createBlog(Blog blog) {
        blog.setBlog_Id(UUID.randomUUID().toString());
        blog.setStatus("DRAFT");                     // always starts as DRAFT
        blog.setRejection_reason(null);
        blog.setCreated_at(LocalDateTime.now());
        blog.setUpdated_at(LocalDateTime.now());
        blogRepository.save(blog);
        return "Blog Created Successfully";
    }

    /**
     * Trainer edits a rejected blog and re-submits.
     * Status is reset to DRAFT and rejection_reason is cleared.
     */
    @Override
    public String updateBlog(String blog_Id, Blog blog) {
        Blog existing = findOrThrow(blog_Id);
        existing.setTitle(blog.getTitle());
        existing.setContent(blog.getContent());
        existing.setAuthor(blog.getAuthor());
        existing.setCategory(blog.getCategory());
        existing.setTags(blog.getTags());
        existing.setFeatured_images(blog.getFeatured_images());
        existing.setThumbnail(blog.getThumbnail());
        existing.setTrainer_id(blog.getTrainer_id());
        // Reset to DRAFT and clear rejection reason on every trainer edit
        existing.setStatus("DRAFT");
        existing.setRejection_reason(null);
        existing.setReviewed_by(null);
        existing.setReviewed_at(null);
        existing.setUpdated_at(LocalDateTime.now());
        blogRepository.save(existing);
        return "Blog Updated Successfully";
    }

    @Override
    public String deleteBlog(String blog_Id) {
        findOrThrow(blog_Id);
        blogRepository.deleteById(blog_Id);
        return "Blog Deleted Successfully";
    }

    // ── Read ───────────────────────────────────────────────────────────────────

    @Override
    public BlogDto getBlogId(String blog_Id) {
        return toDto(findOrThrow(blog_Id));
    }

    @Override
    public List<BlogDto> getAllBlog() {
        return blogRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDto> getBlogsByStatus(String status) {
        return blogRepository.findByStatus(status).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogDto> getBlogsByTrainerId(String trainerId) {
        return blogRepository.findByTrainerId(trainerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ── Admin ──────────────────────────────────────────────────────────────────

    @Override
    public String approveBlog(String blog_Id, String reviewedBy) {
        Blog blog = findOrThrow(blog_Id);
        blog.setStatus("PUBLISHED");
        blog.setRejection_reason(null);
        blog.setReviewed_by(reviewedBy);
        blog.setReviewed_at(LocalDateTime.now().toString());
        blog.setUpdated_at(LocalDateTime.now());
        blogRepository.save(blog);
        return "Blog Approved Successfully";
    }

    @Override
    public String rejectBlog(String blog_Id, String reason, String reviewedBy) {
        Blog blog = findOrThrow(blog_Id);
        blog.setStatus("REJECTED");
        blog.setRejection_reason(reason);
        blog.setReviewed_by(reviewedBy);
        blog.setReviewed_at(LocalDateTime.now().toString());
        blog.setUpdated_at(LocalDateTime.now());
        blogRepository.save(blog);
        return "Blog Rejected Successfully";
    }

    // ── Dashboard counts ───────────────────────────────────────────────────────

    @Override
    public Map<String, Long> getBlogStatusCounts() {
        List<Blog> all = blogRepository.findAll();
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("total",     (long) all.size());
        counts.put("DRAFT",     all.stream().filter(b -> "DRAFT".equals(b.getStatus())).count());
        counts.put("PUBLISHED", all.stream().filter(b -> "PUBLISHED".equals(b.getStatus())).count());
        counts.put("REJECTED",  all.stream().filter(b -> "REJECTED".equals(b.getStatus())).count());
        counts.put("HIDDEN",    all.stream().filter(b -> "HIDDEN".equals(b.getStatus())).count());
        return counts;
    }
}
