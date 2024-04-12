package myapp.authenticateAPI.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Category;
import myapp.authenticateAPI.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> findAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody @Valid Category category, Authentication authentication) {
        return categoryService.processCreateCategory(category, authentication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @RequestBody @Valid Category category,
            Authentication authentication){
        return categoryService.processUpdateCategory(id, category, authentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable String id, Authentication authentication) {
        categoryService.deleteCategory(id, authentication);
        return ResponseEntity.noContent().build();
    }

}
