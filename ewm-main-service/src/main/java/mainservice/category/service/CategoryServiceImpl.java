package mainservice.category.service;


import mainservice.category.dto.CategoryDto;
import mainservice.category.dto.NewCategoryDto;
import mainservice.category.mapper.CategoryMapper;
import mainservice.category.repository.CategoryRepository;
import mainservice.exceptions.ConflictException;
import mainservice.exceptions.NotFoundException;
import mainservice.exceptions.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static mainservice.category.mapper.CategoryMapper.toCategory;
import static mainservice.category.mapper.CategoryMapper.toCategoryDto;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new ValidationException("имя категории не может быть пустым.");
        }

        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Такая категория уже существует.");
        }
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new ValidationException("имя категории не может быть пустым.");
        }

        try {
            return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Такая категория уже существует.");
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Не существует категории: " + catId));

        if (categoryRepository.findEventLinkedWithCategory(catId) != null) {
            throw new ValidationException("Нельзя удалить категорию, с которой связано событие.");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("name").descending());
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Не существует категории: " + catId)));
    }
}
