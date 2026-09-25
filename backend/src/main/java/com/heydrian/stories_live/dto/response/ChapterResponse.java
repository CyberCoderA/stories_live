package com.heydrian.stories_live.dto.response;

import java.time.Instant;

import com.heydrian.stories_live.models.stories_models.Chapters;

public record ChapterResponse(
    String chapterId,
    String storyId,
    int chapterNumber,
    String chapterTitle,
    String chapterContent,
    String chapterStatus,
    int chapterWordCount,
    Instant chapterCreatedAt,
    Instant chapterUpdatedAt,
    Instant chapterPublishedAt
) {
    public static ChapterResponse fromChapter(Chapters chapter) {
        return new ChapterResponse(
            chapter.getChapterId(),
            chapter.getStory() == null ? null : chapter.getStory().getStoryId(),
            chapter.getChapterNumber(),
            chapter.getChapterTitle(),
            chapter.getChapterContent(),
            chapter.getChapterStatus() == null ? null : chapter.getChapterStatus().name(),
            chapter.getChapterWordCount(),
            chapter.getChapterCreatedAt(),
            chapter.getChapterUpdatedAt(),
            chapter.getChapterPublishedAt()
        );
    }
}