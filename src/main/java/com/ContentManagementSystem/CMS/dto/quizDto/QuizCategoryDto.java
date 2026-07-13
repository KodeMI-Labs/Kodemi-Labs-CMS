package com.ContentManagementSystem.CMS.dto.quizDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizCategoryDto {
    private String categoryId;
    private String categoryName;
    private String description;
    private Boolean active;
}
