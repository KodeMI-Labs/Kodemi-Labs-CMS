package com.ContentManagementSystem.CMS.controller;

import com.ContentManagementSystem.CMS.dto.MediaDto;
import com.ContentManagementSystem.CMS.model.Media;
import com.ContentManagementSystem.CMS.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    @PostMapping("/create-dto")
    public Media createMediaDto(
            @RequestBody MediaDto mediaDto) {
        return mediaService.createMedia(mediaDto);
    }
    @PostMapping("/create")
    public String createMedia(
            @RequestBody Media media) {
        return mediaService.createMedia(media);
    }
    @GetMapping("/{media_Id}")
    public MediaDto getMediaById(
            @PathVariable String media_Id) {
        return mediaService.getMediaId(media_Id);
    }
    @GetMapping("/all")
    public List<MediaDto> getAllMedia() {
        return mediaService.getAllMedia();
    }
    @PutMapping("/update/{media_Id}")
    public String updateMedia(
            @PathVariable String media_Id,
            @RequestBody Media media) {
        return mediaService.updateMedia(
                media_Id,
                media
        );
    }
    @DeleteMapping("/delete/{media_Id}")
    public String deleteMedia(
            @PathVariable String media_Id) {
        return mediaService.deleteMedia(media_Id);
    }
}