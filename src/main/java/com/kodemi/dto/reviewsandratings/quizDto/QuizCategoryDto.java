package com.kodemi.dto.reviewsandratings.quizDto;

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
