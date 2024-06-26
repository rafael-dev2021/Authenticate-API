package myapp.authenticateAPI.service.helpers.category;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Category;
import myapp.authenticateAPI.infrastructure.exceptions.CategoryNotFoundException;
import myapp.authenticateAPI.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperComponentUpdateCategory {

    private final CategoryRepository categoryRepository;

    public void helperUpdate(String id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        helperUpdateName(existingCategory, updatedCategory.getName());
        helperUpdateSlug(existingCategory, updatedCategory.getSlug());

        categoryRepository.save(existingCategory);
    }

    public void helperUpdatedCategory(Category category) {
        new Category(category.getName(), category.getSlug());
    }

    private void helperUpdateName(Category existingCategory, String newName) {
        if (newName != null && !newName.equals(existingCategory.getName())) {
            existingCategory.setName(newName);
        }
    }

    private void helperUpdateSlug(Category existingCategory, String newSlug) {
        if (newSlug != null && !newSlug.equals(existingCategory.getSlug())) {
            existingCategory.setSlug(newSlug);
        }
    }
}
