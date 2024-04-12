package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Category;
import myapp.authenticateAPI.infrastructure.exceptions.CategoryNotFoundException;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.repository.CategoryRepository;
import myapp.authenticateAPI.service.helpers.HelperValidateUser;
import myapp.authenticateAPI.service.helpers.category.HelperComponentUpdateCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final HelperValidateUser helperValidateUser;
    private final HelperComponentUpdateCategory helperComponentUpdateCategory;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public ResponseEntity<Category> processCreateCategory(Category category, Authentication authentication) {
        if (!helperValidateUser.isAdmin(authentication)) {
            throw new DomainAccessDeniedException();
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryRepository.save(category));
    }

    public ResponseEntity<Category> processUpdateCategory(String id, Category updatedCategory, Authentication authentication) {
        if (!helperValidateUser.isAdmin(authentication)) {
            throw new DomainAccessDeniedException();
        }

        helperComponentUpdateCategory.helperUpdatedCategory(updatedCategory);
        updatedCategory.setId(id);
        helperComponentUpdateCategory.helperUpdate(id, updatedCategory);

        return ResponseEntity.noContent().build();
    }

    public void deleteCategory(String id, Authentication authentication) {
        if (!helperValidateUser.isAdmin(authentication)) {
            throw new DomainAccessDeniedException();
        }

        Category existingCategory = findById(id);
        categoryRepository.delete(existingCategory);
    }

}
