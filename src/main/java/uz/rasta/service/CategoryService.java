package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.config.ApiException;
import uz.rasta.dto.CategoryDto;
import uz.rasta.entity.Category;
import uz.rasta.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto.Response> listByKind(Category.Kind kind) {
        return categoryRepository.findByKindAndActiveTrueOrderBySortOrder(kind)
                .stream()
                .map(CategoryDto.Response::from)
                .toList();
    }

    @Transactional
    public CategoryDto.Response create(CategoryDto.CreateRequest request) {
        if (categoryRepository.existsByKindAndSlug(request.kind(), request.slug())) {
            throw ApiException.conflict("category.slug.exists");
        }

        Category category = Category.builder()
                .kind(request.kind())
                .slug(request.slug())
                .nameEn(request.nameEn())
                .nameUz(request.nameUz())
                .nameRu(request.nameRu())
                .icon(request.icon())
                .sortOrder(request.sortOrder() != null ? request.sortOrder() : 0)
                .build();

        category = categoryRepository.save(category);
        return CategoryDto.Response.from(category);
    }

    @Transactional
    public CategoryDto.Response update(UUID id, CategoryDto.UpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("category.not.found"));

        if (request.nameEn() != null) category.setNameEn(request.nameEn());
        if (request.nameUz() != null) category.setNameUz(request.nameUz());
        if (request.nameRu() != null) category.setNameRu(request.nameRu());
        if (request.icon() != null) category.setIcon(request.icon());
        if (request.sortOrder() != null) category.setSortOrder(request.sortOrder());
        if (request.active() != null) category.setActive(request.active());

        category = categoryRepository.save(category);
        return CategoryDto.Response.from(category);
    }

    @Transactional
    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("category.not.found"));
        categoryRepository.delete(category);
    }
}
