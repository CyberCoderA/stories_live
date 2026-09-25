package com.heydrian.stories_live.repository.stories_repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.heydrian.stories_live.models.stories_models.Chapters;
import java.util.List;
import com.heydrian.stories_live.models.stories_models.Stories;

public interface ChaptersRepository extends MongoRepository<Chapters, String> {
    Chapters findByChapterId(String chapterId);
    List<Chapters> findAllByStoryOrderByChapterNumberAsc(Stories story);
    boolean existsByStoryAndChapterNumber(Stories story, int chapterNumber);
    boolean existsByStoryAndChapterNumberAndChapterIdNot(Stories story, int chapterNumber, String chapterId);
}
