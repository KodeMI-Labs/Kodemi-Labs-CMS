package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.BlogDto;
import com.ContentManagementSystem.CMS.model.Blog;

import java.util.List;

public interface BlogService {
    Blog createBlog(BlogDto blogDto);
    String createBlog(Blog blog);
    BlogDto getBlogId(String blog_Id);
    List<BlogDto>getAllBlog();
    String updateBlog(String blog_Id,Blog blog);
    String deleteBlog(String blog_Id);
}
