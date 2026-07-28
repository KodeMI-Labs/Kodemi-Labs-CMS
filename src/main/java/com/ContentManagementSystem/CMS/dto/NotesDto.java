package com.ContentManagementSystem.CMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotesDto {
    private String note_id;
    private String title;
    private String description;
    private String attachment_url;
    private String visibility;
}
