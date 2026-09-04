package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.rasta.config.ApiResponse;
import uz.rasta.dto.CategoryDto;
import uz.rasta.entity.Category;
import uz.rasta.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto.Response>>> list(@RequestParam Category.Kind kind) {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.listByKind(kind)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto.Response>> create(
            @Valid @RequestBody CategoryDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(categoryService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto.Response>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryDto.UpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
