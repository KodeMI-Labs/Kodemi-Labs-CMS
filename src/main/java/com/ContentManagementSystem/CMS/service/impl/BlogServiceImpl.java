package com.ContentManagementSystem.CMS.service.impl;

import com.ContentManagementSystem.CMS.dto.BlogDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Blog;
import com.ContentManagementSystem.CMS.repository.BlogRepository;
import com.ContentManagementSystem.CMS.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    @Override
    public Blog createBlog(BlogDto blogDto) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDto, blog);
        blog.setBlog_Id(UUID.randomUUID().toString());
        blog.setCreated_at(LocalDateTime.now());
        blog.setUpdated_at(LocalDateTime.now());
        return blogRepository.save(blog);
    }
    @Override
    public String createBlog(Blog blog) {
        blog.setBlog_Id(UUID.randomUUID().toString());
        blog.setCreated_at(LocalDateTime.now());
        blog.setUpdated_at(LocalDateTime.now());
        blogRepository.save(blog);
        return "Blog Created Successfully";
    }
    @Override
    public BlogDto getBlogId(String blog_Id) {
        Blog blog = blogRepository.findById(blog_Id);
        if (blog == null) {
            throw new ResourceNotFoundException("Blog not found with id: " + blog_Id);
        }
        BlogDto blogDto = new BlogDto();
        BeanUtils.copyProperties(blog, blogDto);
        // Fallback: use first featured_image as thumbnail if thumbnail is not set
        if (blogDto.getThumbnail() == null &&
                blog.getFeatured_images() != null &&
                !blog.getFeatured_images().isEmpty()) {
            blogDto.setThumbnail(blog.getFeatured_images().get(0));
        }
        return blogDto;
    }

    @Override
    public List<BlogDto> getAllBlog() {
        List<Blog> blogs = blogRepository.findAll();
        List<BlogDto> blogDtoList = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogDto blogDto = new BlogDto();
            BeanUtils.copyProperties(blog, blogDto);
            // Fallback: use first featured_image as thumbnail if thumbnail is not set
            if (blogDto.getThumbnail() == null &&
                    blog.getFeatured_images() != null &&
                    !blog.getFeatured_images().isEmpty()) {
                blogDto.setThumbnail(blog.getFeatured_images().get(0));
            }
            blogDtoList.add(blogDto);
        }
        return blogDtoList;
    }
    @Override
    public String updateBlog(String blog_Id, Blog blog) {
        Blog existingBlog = blogRepository.findById(blog_Id);
        if (existingBlog == null) {
            throw new ResourceNotFoundException("Blog not found with id: " + blog_Id);
        }
        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());
        existingBlog.setAuthor(blog.getAuthor());
        existingBlog.setCategory(blog.getCategory());
        existingBlog.setTags(blog.getTags());
        existingBlog.setFeatured_images(blog.getFeatured_images());
        existingBlog.setStatus(blog.getStatus());
        existingBlog.setThumbnail(blog.getThumbnail());
        existingBlog.setTrainer_id(blog.getTrainer_id());
        existingBlog.setUpdated_at(LocalDateTime.now());
        blogRepository.save(existingBlog);
        return "Blog Updated Successfully";
    }
    @Override
    public String deleteBlog(String blog_Id) {
        Blog blog = blogRepository.findById(blog_Id);
        if (blog == null) {
            throw new ResourceNotFoundException("Blog not found with id: " + blog_Id);
        }
        blogRepository.deleteById(blog_Id);
        return "Blog Deleted Successfully";
    }
}