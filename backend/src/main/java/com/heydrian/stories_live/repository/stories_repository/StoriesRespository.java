package com.heydrian.stories_live.repository.stories_repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.heydrian.stories_live.models.stories_models.Stories;

public interface StoriesRespository extends MongoRepository<Stories, String> {
    public Stories findByStoryId(String storyId);
    public Stories findByStorySlugAndStoryAuthor(String storySlug, String storyAuthor);
}