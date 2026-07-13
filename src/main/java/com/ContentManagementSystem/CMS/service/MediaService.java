package com.ContentManagementSystem.CMS.service;

import com.ContentManagementSystem.CMS.dto.MediaDto;
import com.ContentManagementSystem.CMS.model.Media;

import java.util.List;

public interface MediaService {
    Media createMedia(MediaDto mediaDto);
    String createMedia(Media media);
    MediaDto getMediaId(String media_Id);
    List<MediaDto> getAllMedia();
    String updateMedia(String media_Id,Media media);
    String deleteMedia(String media_Id);
}
