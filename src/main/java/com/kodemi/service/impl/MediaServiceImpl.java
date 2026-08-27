package com.kodemi.service.impl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.MediaDto;
import com.kodemi.model.Media;
import com.kodemi.repository.MediaRepository;
import com.kodemi.service.MediaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    @Override
    public Media createMedia(MediaDto mediaDto) {
        Media media = new Media();
        BeanUtils.copyProperties(mediaDto, media);
        media.setMeta_Id(UUID.randomUUID().toString());
        media.setCreated_at(LocalDateTime.now());
        media.setUpdated_at(LocalDateTime.now());
        mediaRepository.save(media);
        return media;
    }
    @Override
    public String createMedia(Media media) {
        media.setMeta_Id(UUID.randomUUID().toString());
        media.setCreated_at(LocalDateTime.now());
        media.setUpdated_at(LocalDateTime.now());
        mediaRepository.save(media);
        return "Media Created Successfully";
    }
    @Override
    public MediaDto getMediaId(String media_Id) {
        Media media = mediaRepository.findById(media_Id);
        if (media == null) {
            throw new ResourceNotFoundException("Media not found with id: " + media_Id);
        }
        MediaDto mediaDto = new MediaDto();
        BeanUtils.copyProperties(media, mediaDto);
        return mediaDto;
    }
    @Override
    public List<MediaDto> getAllMedia() {
        List<Media> mediaList = mediaRepository.findAll();
        List<MediaDto> mediaDtoList = new ArrayList<>();
        for (Media media : mediaList) {
            MediaDto mediaDto = new MediaDto();
            BeanUtils.copyProperties(media, mediaDto);
            mediaDtoList.add(mediaDto);
        }
        return mediaDtoList;
    }
    @Override
    public String updateMedia(String media_Id, Media media) {
        Media existingMedia = mediaRepository.findById(media_Id);
        if (existingMedia == null) {
            throw new ResourceNotFoundException("Media not found with id: " + media_Id);
        }
        existingMedia.setFile_name(media.getFile_name());
        existingMedia.setFile_type(media.getFile_type());
        existingMedia.setFile_url(media.getFile_url());
        existingMedia.setUploaded_by(media.getUploaded_by());
        existingMedia.setUpdated_at(LocalDateTime.now());
        mediaRepository.save(existingMedia);
        return "Media Updated Successfully";
    }
    @Override
    public String deleteMedia(String media_Id) {
        Media media = mediaRepository.findById(media_Id);
        if (media == null) {
            throw new ResourceNotFoundException("Media not found with id: " + media_Id);
        }
        mediaRepository.delete(media_Id);
        return "Media Deleted Successfully";
    }
}