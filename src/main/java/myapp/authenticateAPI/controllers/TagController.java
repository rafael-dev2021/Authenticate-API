package myapp.authenticateAPI.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Tag;
import myapp.authenticateAPI.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> findAllTags() {
        return ResponseEntity.ok(tagService.findAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> findByIdTag(@PathVariable String id) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Tag> createTag(@RequestBody @Valid Tag tag, Authentication authentication) {
        return tagService.processCreateTag(tag, authentication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(
            @PathVariable String id,
            @RequestBody @Valid Tag tag,
            Authentication authentication
    ) {
        return tagService.processUpdateTag(id, tag, authentication);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable String id, Authentication authentication) {
        tagService.deleteTag(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
