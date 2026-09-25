package com.heydrian.stories_live.dto.response;

import java.time.Instant;
import java.util.List;

import com.heydrian.stories_live.models.stories_models.Stories;

public record StoryResponse(
    String storyId,
    String storyAuthor,
    String storyTitle,
    String storySlug,
    String storyDescription,
    String storyStatus,
    List<String> storyTags,
    Instant storyCreatedAt,
    Instant storyUpdatedAt,
    Instant storyPublishedAt
) {
    public static StoryResponse fromStory(Stories story) {
        return new StoryResponse(
            story.getStoryId(),
            story.getStoryAuthor(),
            story.getStoryTitle(),
            story.getStorySlug(),
            story.getStoryDescription(),
            story.getStoryStatus() == null ? null : story.getStoryStatus().name(),
            story.getStoryTags(),
            story.getStoryCreatedAt(),
            story.getStoryUpdatedAt(),
            story.getStoryPublishedAt()
        );
    }
}
