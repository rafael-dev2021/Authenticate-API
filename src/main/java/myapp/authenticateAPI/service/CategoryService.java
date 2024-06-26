package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Category;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.infrastructure.exceptions.CategoryNotFoundException;
import myapp.authenticateAPI.repository.CategoryRepository;
import myapp.authenticateAPI.service.helpers.category.HelperComponentUpdateCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final HelperComponentUpdateCategory helperComponentUpdateCategory;

    public List<Post> findPostsByCategoryId(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return category.getPosts();
    }
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public ResponseEntity<Category> processCreateCategory(Category category) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(uri).body(categoryRepository.save(category));
    }

    public ResponseEntity<Category> processUpdateCategory(String id, Category updatedCategory) {
        helperComponentUpdateCategory.helperUpdatedCategory(updatedCategory);
        updatedCategory.setId(id);
        helperComponentUpdateCategory.helperUpdate(id, updatedCategory);

        return ResponseEntity.noContent().build();
    }

    public void deleteCategory(String id) {
        Category existingCategory = findById(id);
        categoryRepository.delete(existingCategory);
    }
}
