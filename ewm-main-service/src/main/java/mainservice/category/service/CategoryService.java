package mainservice.category.service;

import mainservice.category.dto.CategoryDto;
import mainservice.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
