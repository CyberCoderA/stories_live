package com.heydrian.stories_live.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.heydrian.stories_live.enums.ChapterStatus;
import com.heydrian.stories_live.enums.StoryStatus;
import com.heydrian.stories_live.models.stories_models.Chapters;
import com.heydrian.stories_live.models.stories_models.Stories;
import com.heydrian.stories_live.repository.stories_repository.ChaptersRepository;
import com.heydrian.stories_live.repository.stories_repository.StoriesRespository;

@ExtendWith(MockitoExtension.class)
class ChapterServiceTest {

    @Mock
    private ChaptersRepository chaptersRepository;

    @Mock
    private StoriesRespository storiesRepository;

    @Test
    void publish_shouldRejectChapterWhenStoryIsDraft() {
        Stories story = new Stories();
        story.setStoryId("story-1");
        story.setStoryAuthor("alice");
        story.setStoryStatus(StoryStatus.DRAFT);

        Chapters chapter = new Chapters();
        chapter.setChapterId("chapter-1");
        chapter.setStory(story);
        chapter.setChapterStatus(ChapterStatus.DRAFT);

        when(chaptersRepository.findByChapterId("chapter-1")).thenReturn(chapter);
        ChapterService chapterService = new ChapterService(chaptersRepository, storiesRepository);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> chapterService.publish("chapter-1", "alice")
        );

        verify(chaptersRepository, never()).save(chapter);
        org.junit.jupiter.api.Assertions.assertEquals(409, exception.getStatusCode().value());
    }
}