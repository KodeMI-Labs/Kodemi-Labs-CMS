package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.BlogDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Blog;
import com.ContentManagementSystem.CMS.repository.BlogRepository;
import com.ContentManagementSystem.CMS.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    private Blog blog;

    @BeforeEach
    void setUp() {
        blog = new Blog();
        blog.setBlog_Id("blog1");
        blog.setTitle("Test Blog");
        blog.setAuthor("Author");
        blog.setStatus("PUBLISHED");
    }

    @Test
    void createBlog_withDto_returnsBlog() {
        BlogDto dto = new BlogDto();
        dto.setBlog_Id("blog1");
        dto.setTitle("Test Blog");
        when(blogRepository.save(any(Blog.class))).thenAnswer(i -> i.getArgument(0));

        Blog result = blogService.createBlog(dto);

        assertNotNull(result);
        assertEquals("Test Blog", result.getTitle());
    }

    @Test
    void createBlog_withModel_returnsSuccessMessage() {
        when(blogRepository.save(blog)).thenReturn(blog);

        String result = blogService.createBlog(blog);

        assertEquals("Blog Created Successfully", result);
    }

    @Test
    void getBlogId_found_returnsDto() {
        when(blogRepository.findById("blog1")).thenReturn(blog);

        BlogDto result = blogService.getBlogId("blog1");

        assertNotNull(result);
        assertEquals("Test Blog", result.getTitle());
    }

    @Test
    void getBlogId_notFound_throwsException() {
        when(blogRepository.findById("blog1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> blogService.getBlogId("blog1"));
    }

    @Test
    void getAllBlog_returnsList() {
        when(blogRepository.findAll()).thenReturn(List.of(blog));

        List<BlogDto> result = blogService.getAllBlog();

        assertEquals(1, result.size());
    }

    @Test
    void updateBlog_found_returnsSuccessMessage() {
        when(blogRepository.findById("blog1")).thenReturn(blog);
        when(blogRepository.save(any())).thenReturn(blog);

        String result = blogService.updateBlog("blog1", blog);

        assertEquals("Blog Updated Successfully", result);
    }

    @Test
    void updateBlog_notFound_throwsException() {
        when(blogRepository.findById("blog1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> blogService.updateBlog("blog1", blog));
    }

    @Test
    void deleteBlog_found_returnsSuccessMessage() {
        when(blogRepository.findById("blog1")).thenReturn(blog);

        String result = blogService.deleteBlog("blog1");

        assertEquals("Blog Deleted Successfully", result);
        verify(blogRepository).deleteById("blog1");
    }

    @Test
    void deleteBlog_notFound_throwsException() {
        when(blogRepository.findById("blog1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> blogService.deleteBlog("blog1"));
    }
}
