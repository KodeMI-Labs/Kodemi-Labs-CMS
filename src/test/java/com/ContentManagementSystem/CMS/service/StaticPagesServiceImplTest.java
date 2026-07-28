package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.StaticPagesDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.StaticPages;
import com.ContentManagementSystem.CMS.repository.StaticPagesRepository;
import com.ContentManagementSystem.CMS.service.impl.StaticPagesServiceImpl;
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
class StaticPagesServiceImplTest {

    @Mock
    private StaticPagesRepository staticPagesRepository;

    @InjectMocks
    private StaticPagesServiceImpl staticPagesService;

    private StaticPages staticPages;

    @BeforeEach
    void setUp() {
        staticPages = new StaticPages();
        staticPages.setPage_id("p1");
        staticPages.setTitle("About Us");
        staticPages.setSlug("about-us");
        staticPages.setContent("About us content");
        staticPages.setStatus("PUBLISHED");
    }

    @Test
    void createStaticPages_withDto_returnsStaticPages() {
        StaticPagesDto dto = new StaticPagesDto();
        dto.setPage_Id("p1");
        dto.setTitle("About Us");
        when(staticPagesRepository.save(any(StaticPages.class))).thenAnswer(i -> i.getArgument(0));

        StaticPages result = staticPagesService.createStaticPages(dto);

        assertNotNull(result);
        assertEquals("About Us", result.getTitle());
    }

    @Test
    void createStaticPages_withModel_returnsSuccessMessage() {
        when(staticPagesRepository.save(staticPages)).thenReturn(staticPages);

        String result = staticPagesService.createStaticPages(staticPages);

        assertEquals("Static Page Created Successfully", result);
    }

    @Test
    void getAllStaticPages_byId_found_returnsDto() {
        when(staticPagesRepository.findById("p1")).thenReturn(staticPages);

        StaticPagesDto result = staticPagesService.getAllStaticPages("p1");

        assertNotNull(result);
        assertEquals("About Us", result.getTitle());
    }

    @Test
    void getAllStaticPages_byId_notFound_throwsException() {
        when(staticPagesRepository.findById("p1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> staticPagesService.getAllStaticPages("p1"));
    }

    @Test
    void getAllStaticPages_returnsList() {
        when(staticPagesRepository.findAll()).thenReturn(List.of(staticPages));

        List<StaticPagesDto> result = staticPagesService.getAllStaticPages();

        assertEquals(1, result.size());
    }

    @Test
    void updateStaticPages_found_returnsSuccessMessage() {
        when(staticPagesRepository.findById("p1")).thenReturn(staticPages);
        when(staticPagesRepository.save(any())).thenReturn(staticPages);

        String result = staticPagesService.updateStaticPages("p1", staticPages);

        assertEquals("Static Page Updated Successfully", result);
    }

    @Test
    void updateStaticPages_notFound_throwsException() {
        when(staticPagesRepository.findById("p1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> staticPagesService.updateStaticPages("p1", staticPages));
    }

    @Test
    void deleteStaticPages_found_returnsSuccessMessage() {
        when(staticPagesRepository.findById("p1")).thenReturn(staticPages);

        String result = staticPagesService.deleteStaticPages("p1");

        assertEquals("Static Page Deleted Successfully", result);
        verify(staticPagesRepository).delete("p1");
    }

    @Test
    void deleteStaticPages_notFound_throwsException() {
        when(staticPagesRepository.findById("p1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> staticPagesService.deleteStaticPages("p1"));
    }
}
