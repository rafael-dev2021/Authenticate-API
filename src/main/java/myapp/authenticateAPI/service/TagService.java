package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.Tag;
import myapp.authenticateAPI.infrastructure.exceptions.TagNotFoundException;
import myapp.authenticateAPI.repository.TagRepository;
import myapp.authenticateAPI.service.helpers.HelperValidateUser;
import myapp.authenticateAPI.service.helpers.tag.HelperComponentUpdateTag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final HelperComponentUpdateTag helperComponentUpdateTag;

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findById(String id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.orElseThrow(() -> new TagNotFoundException(id));
    }

    public ResponseEntity<Tag> processCreateTag(Tag tag) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tag.getId())
                .toUri();
        return ResponseEntity.created(uri).body(tagRepository.insert(tag));
    }

    public ResponseEntity<Tag> processUpdateTag(String id, Tag updatedTag) {
        helperComponentUpdateTag.helperUpdatedTag(updatedTag);
        updatedTag.setId(id);
        helperComponentUpdateTag.helperUpdate(id, updatedTag);

        return ResponseEntity.noContent().build();
    }

    public void deleteTag(String id) {
        Tag existingTag = findById(id);
        tagRepository.delete(existingTag);
    }

    public List<Post> findPostsByTagId(String tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        return tag.getPosts();
    }
}
