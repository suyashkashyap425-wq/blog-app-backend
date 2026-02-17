package com.codewithdurgesh.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {

    private Integer categoryId;

    @NotBlank(message = "Category title must not be blank")
    @Size(min = 4, message = "Category title must be at least 4 characters")
    private String categoryTitle;

    @NotBlank(message = "Category description must not be blank")
    @Size(min = 10, message = "Category description must be at least 10 characters")
    private String categoryDescription;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
