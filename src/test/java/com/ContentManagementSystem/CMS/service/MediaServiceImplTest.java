package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.MediaDto;
import com.ContentManagementSystem.CMS.exception.ResourceNotFoundException;
import com.ContentManagementSystem.CMS.model.Media;
import com.ContentManagementSystem.CMS.repository.MediaRepository;
import com.ContentManagementSystem.CMS.service.impl.MediaServiceImpl;
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
class MediaServiceImplTest {

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    private MediaServiceImpl mediaService;

    private Media media;

    @BeforeEach
    void setUp() {
        media = new Media();
        media.setMeta_Id("m1");
        media.setFile_name("image.png");
        media.setFile_type("IMAGE");
        media.setFile_url("http://example.com/image.png");
        media.setUploaded_by("user1");
    }

    @Test
    void createMedia_withDto_returnsMedia() {
        MediaDto dto = new MediaDto();
        dto.setMeta_Id("m1");
        dto.setFile_name("image.png");
        when(mediaRepository.save(any(Media.class))).thenAnswer(i -> i.getArgument(0));

        Media result = mediaService.createMedia(dto);

        assertNotNull(result);
        assertEquals("image.png", result.getFile_name());
    }

    @Test
    void createMedia_withModel_returnsSuccessMessage() {
        when(mediaRepository.save(media)).thenReturn(media);

        String result = mediaService.createMedia(media);

        assertEquals("Media Created Successfully", result);
    }

    @Test
    void getMediaId_found_returnsDto() {
        when(mediaRepository.findById("m1")).thenReturn(media);

        MediaDto result = mediaService.getMediaId("m1");

        assertNotNull(result);
        assertEquals("image.png", result.getFile_name());
    }

    @Test
    void getMediaId_notFound_throwsException() {
        when(mediaRepository.findById("m1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> mediaService.getMediaId("m1"));
    }

    @Test
    void getAllMedia_returnsList() {
        when(mediaRepository.findAll()).thenReturn(List.of(media));

        List<MediaDto> result = mediaService.getAllMedia();

        assertEquals(1, result.size());
    }

    @Test
    void updateMedia_found_returnsSuccessMessage() {
        when(mediaRepository.findById("m1")).thenReturn(media);
        when(mediaRepository.save(any())).thenReturn(media);

        String result = mediaService.updateMedia("m1", media);

        assertEquals("Media Updated Successfully", result);
    }

    @Test
    void updateMedia_notFound_throwsException() {
        when(mediaRepository.findById("m1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> mediaService.updateMedia("m1", media));
    }

    @Test
    void deleteMedia_found_returnsSuccessMessage() {
        when(mediaRepository.findById("m1")).thenReturn(media);

        String result = mediaService.deleteMedia("m1");

        assertEquals("Media Deleted Successfully", result);
        verify(mediaRepository).delete("m1");
    }

    @Test
    void deleteMedia_notFound_throwsException() {
        when(mediaRepository.findById("m1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> mediaService.deleteMedia("m1"));
    }
}
