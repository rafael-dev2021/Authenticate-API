package myapp.authenticateAPI.service.helpers.tag;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Tag;
import myapp.authenticateAPI.infrastructure.exceptions.TagNotFoundException;
import myapp.authenticateAPI.repository.TagRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperComponentUpdateTag {

    private final TagRepository tagRepository;

    public void helperUpdate(String id, Tag updatedTag) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        helperUpdateName(existingTag, updatedTag.getName());
        helperUpdateSlug(existingTag, updatedTag.getSlug());

        tagRepository.save(existingTag);
    }
    public void helperUpdatedTag(Tag tag) {
        new Tag(tag.getName(), tag.getSlug());
    }

    private void helperUpdateName(Tag existingTag, String newName) {
        if (newName != null && !newName.equals(existingTag.getName())) {
            existingTag.setName(newName);
        }
    }

    private void helperUpdateSlug(Tag existingTag, String newSlug) {
        if (newSlug != null && !newSlug.equals(existingTag.getSlug())) {
            existingTag.setSlug(newSlug);
        }
    }
}
