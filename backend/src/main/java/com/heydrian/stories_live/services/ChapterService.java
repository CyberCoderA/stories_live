package com.heydrian.stories_live.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.heydrian.stories_live.dto.request.CreateChapterRequest;
import com.heydrian.stories_live.dto.request.UpdateChapterRequest;
import com.heydrian.stories_live.enums.ChapterStatus;
import com.heydrian.stories_live.enums.StoryStatus;
import com.heydrian.stories_live.models.stories_models.Chapters;
import com.heydrian.stories_live.models.stories_models.Stories;
import com.heydrian.stories_live.repository.stories_repository.ChaptersRepository;
import com.heydrian.stories_live.repository.stories_repository.StoriesRespository;

@Service
public class ChapterService {
    private final ChaptersRepository chaptersRepository;
    private final StoriesRespository storiesRepository;

    public ChapterService(ChaptersRepository chaptersRepository, StoriesRespository storiesRepository) {
        this.chaptersRepository = chaptersRepository;
        this.storiesRepository = storiesRepository;
    }

    public Chapters create(CreateChapterRequest request, String author) {
        Stories story = ownedStory(request.storyId(), author);
        if (chaptersRepository.existsByStoryAndChapterNumber(story, request.chapterNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chapter number already exists");
        }

        Instant now = Instant.now();
        Chapters chapter = new Chapters(
            UUID.randomUUID().toString().replace("-", "").substring(0, 12),
            story,
            request.chapterNumber(),
            request.chapterTitle().trim(),
            request.chapterContent().trim(),
            ChapterStatus.DRAFT,
            request.chapterWordCount(),
            now,
            now,
            null
        );
        return chaptersRepository.save(chapter);
    }

    public Chapters get(String chapterId, String author) {
        Chapters chapter = findChapter(chapterId);
        assertOwner(chapter, author);
        return chapter;
    }

    public List<Chapters> listByStory(String storyId, String author) {
        Stories story = ownedStory(storyId, author);
        return chaptersRepository.findAllByStoryOrderByChapterNumberAsc(story);
    }

    public Chapters update(String chapterId, UpdateChapterRequest request, String author) {
        Chapters chapter = get(chapterId, author);
        if (chapter.getChapterStatus() == ChapterStatus.PUBLISHED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Published chapters cannot be edited");
        }
        if (chaptersRepository.existsByStoryAndChapterNumberAndChapterIdNot(
            chapter.getStory(), request.chapterNumber(), chapterId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chapter number already exists");
        }

        chapter.setChapterNumber(request.chapterNumber());
        chapter.setChapterTitle(request.chapterTitle().trim());
        chapter.setChapterContent(request.chapterContent().trim());
        chapter.setChapterWordCount(request.chapterWordCount());
        chapter.setChapterUpdatedAt(Instant.now());
        return chaptersRepository.save(chapter);
    }

    public Chapters publish(String chapterId, String author) {
        Chapters chapter = get(chapterId, author);
        Stories story = chapter.getStory();
        if (story.getStoryStatus() == StoryStatus.DRAFT
            || story.getStoryPublishedAt() == null) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "The linked story must be published before publishing a chapter"
            );
        }

        Instant now = Instant.now();
        chapter.setChapterStatus(ChapterStatus.PUBLISHED);
        chapter.setChapterPublishedAt(now);
        chapter.setChapterUpdatedAt(now);
        return chaptersRepository.save(chapter);
    }

    public void delete(String chapterId, String author) {
        Chapters chapter = get(chapterId, author);
        chaptersRepository.delete(chapter);
    }

    private Stories ownedStory(String storyId, String author) {
        Stories story = storiesRepository.findByStoryId(storyId);
        if (story == null || !author.equals(story.getStoryAuthor())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
        }
        return story;
    }

    private Chapters findChapter(String chapterId) {
        Chapters chapter = chaptersRepository.findByChapterId(chapterId);
        if (chapter == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found");
        }
        return chapter;
    }

    private void assertOwner(Chapters chapter, String author) {
        if (chapter.getStory() == null || !author.equals(chapter.getStory().getStoryAuthor())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found");
        }
    }
}