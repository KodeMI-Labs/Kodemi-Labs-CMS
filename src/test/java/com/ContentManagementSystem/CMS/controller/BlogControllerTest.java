package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.BlogDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Blog;
import com.ContentManagementSystem.CMS.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.ContentManagementSystem.CMS.config.JwtUtil;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@AutoConfigureMockMvc(addFilters = false)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlogService blogService;

    @MockBean
    private JwtUtil jwtUtil;

    private Blog blog;
    private BlogDto blogDto;

    @BeforeEach
    void setUp() {
        blog = new Blog();
        blog.setBlog_Id("blog1");
        blog.setTitle("My Blog");
        blog.setAuthor("John");
        blog.setStatus("PUBLISHED");

        blogDto = new BlogDto();
        blogDto.setBlog_Id("blog1");
        blogDto.setTitle("My Blog");
        blogDto.setAuthor("John");
        blogDto.setStatus("PUBLISHED");
    }

    @Test
    void createBlog_returnsSuccessMessage() throws Exception {
        when(blogService.createBlog(any(Blog.class))).thenReturn("Blog Created Successfully");

        mockMvc.perform(post("/blog/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isOk())
                .andExpect(content().string("Blog Created Successfully"));
    }

    @Test
    void createBlogDto_returnsBlog() throws Exception {
        when(blogService.createBlog(any(BlogDto.class))).thenReturn(blog);

        mockMvc.perform(post("/blog/create-dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blogDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blog_Id").value("blog1"))
                .andExpect(jsonPath("$.title").value("My Blog"));
    }

    @Test
    void getBlogById_found_returnsDto() throws Exception {
        when(blogService.getBlogId("blog1")).thenReturn(blogDto);

        mockMvc.perform(get("/blog/blog1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blog_Id").value("blog1"))
                .andExpect(jsonPath("$.title").value("My Blog"));
    }

    @Test
    void getBlogById_notFound_returns404() throws Exception {
        when(blogService.getBlogId("blog1")).thenThrow(new ResourceNotFoundException("Blog not found with id: blog1"));

        mockMvc.perform(get("/blog/blog1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Blog not found with id: blog1"));
    }

    @Test
    void getAllBlogs_returnsList() throws Exception {
        when(blogService.getAllBlog()).thenReturn(List.of(blogDto));

        mockMvc.perform(get("/blog/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("My Blog"));
    }

    @Test
    void updateBlog_found_returnsSuccessMessage() throws Exception {
        when(blogService.updateBlog(eq("blog1"), any(Blog.class))).thenReturn("Blog Updated Successfully");

        mockMvc.perform(put("/blog/update/blog1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isOk())
                .andExpect(content().string("Blog Updated Successfully"));
    }

    @Test
    void updateBlog_notFound_returns404() throws Exception {
        when(blogService.updateBlog(eq("blog1"), any(Blog.class)))
                .thenThrow(new ResourceNotFoundException("Blog not found with id: blog1"));

        mockMvc.perform(put("/blog/update/blog1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Blog not found with id: blog1"));
    }

    @Test
    void deleteBlog_found_returnsSuccessMessage() throws Exception {
        when(blogService.deleteBlog("blog1")).thenReturn("Blog Deleted Successfully");

        mockMvc.perform(delete("/blog/delete/blog1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Blog Deleted Successfully"));
    }

    @Test
    void deleteBlog_notFound_returns404() throws Exception {
        when(blogService.deleteBlog("blog1")).thenThrow(new ResourceNotFoundException("Blog not found with id: blog1"));

        mockMvc.perform(delete("/blog/delete/blog1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Blog not found with id: blog1"));
    }
}
