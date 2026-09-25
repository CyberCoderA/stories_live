package com.heydrian.stories_live.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.heydrian.stories_live.dto.request.CreateStoryRequest;
import com.heydrian.stories_live.enums.StoryStatus;
import com.heydrian.stories_live.models.stories_models.Stories;
import com.heydrian.stories_live.repository.stories_repository.StoriesRespository;

import java.time.Instant;
import java.util.Locale;
import java.util.List;

@Service 
public class StoryService {
    private final StoriesRespository storyRepo;

    public StoryService(StoriesRespository storyRepo) {
        this.storyRepo = storyRepo;
    }

    public String toSlug(String title) {
        return title.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "-");
    }

    public String generateStoryID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    public Stories createStory(CreateStoryRequest request, String author) {
        String title = request.title().trim();
        String slug = toSlug(title);
        if (storyRepo.findByStorySlugAndStoryAuthor(slug, author) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Story already exists");
        }

        Instant now = Instant.now();
        Stories story = new Stories(
            generateStoryID(),
            author,
            title,
            slug,
            request.description().trim(),
            StoryStatus.DRAFT,
            request.tags() == null ? List.of() : request.tags(),
            now,
            now,
            null
        );

        return storyRepo.save(story);
    }

    public Stories getStory(String id, String author) {
        Stories story = storyRepo.findByStoryId(id);
        if (story == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
        }
        if (author != null && !author.equals(story.getStoryAuthor())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
        }
        return story;
    }
}
