package com.heydrian.stories_live.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.heydrian.stories_live.models.stories_models.Stories;
import com.heydrian.stories_live.repository.stories_repository.StoriesRespository;

@ExtendWith(MockitoExtension.class)
class StoryServiceTest {

    @Mock
    private StoriesRespository storyRepository;

    @Test
    void getStory_shouldReturnStoryWhenUserOwnsIt() {
        Stories story = new Stories();
        story.setStoryId("story-1");
        story.setStoryAuthor("alice");

        when(storyRepository.findByStoryId("story-1")).thenReturn(story);

        StoryService storyService = new StoryService(storyRepository);
        Stories result = storyService.getStory("story-1", "alice");

        assertNotNull(result);
        assertEquals("story-1", result.getStoryId());
    }

    @Test
    void getStory_shouldRejectStoryOwnedByAnotherUser() {
        Stories story = new Stories();
        story.setStoryId("story-1");
        story.setStoryAuthor("alice");

        when(storyRepository.findByStoryId("story-1")).thenReturn(story);

        StoryService storyService = new StoryService(storyRepository);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> storyService.getStory("story-1", "bob")
        );

        assertEquals(404, exception.getStatusCode().value());
    }
}
