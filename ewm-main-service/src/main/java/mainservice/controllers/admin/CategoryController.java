package mainservice.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import mainservice.category.dto.CategoryDto;
import mainservice.category.dto.NewCategoryDto;
import mainservice.category.service.CategoryServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public CategoryDto saveCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Creating category={}", newCategoryDto);
        return categoryService.saveCategory(newCategoryDto);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Creating category={}", newCategoryDto);
        return categoryService.updateCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Delete category={}", catId);
        categoryService.deleteCategory(catId);
    }
}
