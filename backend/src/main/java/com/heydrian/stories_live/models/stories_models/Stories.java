package com.heydrian.stories_live.models.stories_models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import com.heydrian.stories_live.enums.StoryStatus;

@Document(collection = "stories")
@CompoundIndex(def = "{'story_author': 1, 'story_slug': 1}", unique = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stories {

    @Id
    private String storyId;

    @Field("story_author")
    private String storyAuthor;

    @Field("story_title")
    private String storyTitle;

    @Field("story_slug")
    private String storySlug;

    @Field("story_description")
    private String storyDescription;

    @Field("story_status")
    private StoryStatus storyStatus;

    @Field("story_tags")
    private List<String> storyTags;

    @Field("story_created_at")
    private Instant storyCreatedAt;

    @Field("story_updated_at")
    private Instant storyUpdatedAt;

    @Field("story_published_at")
    private Instant storyPublishedAt;
}
