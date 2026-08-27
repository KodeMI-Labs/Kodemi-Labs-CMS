package com.kodemi.service;

import java.util.List;

import com.kodemi.dto.MediaDto;
import com.kodemi.model.Media;

public interface MediaService {
    Media createMedia(MediaDto mediaDto);
    String createMedia(Media media);
    MediaDto getMediaId(String media_Id);
    List<MediaDto> getAllMedia();
    String updateMedia(String media_Id,Media media);
    String deleteMedia(String media_Id);
}
