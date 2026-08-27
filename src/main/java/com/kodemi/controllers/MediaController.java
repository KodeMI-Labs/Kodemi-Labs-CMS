package com.kodemi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodemi.dto.MediaDto;
import com.kodemi.model.Media;
import com.kodemi.service.MediaService;

import lombok.RequiredArgsConstructor;

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