package mainservice.controllers.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.category.dto.CategoryDto;
import mainservice.category.service.CategoryServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Get category={}", catId);
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from",
            defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(name = "size",
                                                   defaultValue = "10") @Positive Integer size) {
        log.info("Get sorted categories from={}, sizer={}", from, size);
        return categoryService.getAllCategories(from, size);
    }
}
