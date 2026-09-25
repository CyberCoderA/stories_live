package com.heydrian.stories_live.models.stories_models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.heydrian.stories_live.enums.ChapterStatus;
import com.mongodb.lang.Nullable;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "chapters")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class Chapters {
    
    @Id
    private String chapterId;

    @DBRef
    private Stories story;

    @Field("chapter_number")
    @Nullable
    private int chapterNumber;

    @Field("chapter_title")
    private String chapterTitle;

    @Field("chapter_content")
    private String chapterContent;

    @Enumerated(EnumType.STRING)
    @Field("chapter_status")
    private ChapterStatus chapterStatus;

    @Field("chapter_word_count")
    private int chapterWordCount;

    @Field("chapter_createdAt")
    private Instant chapterCreatedAt;

    @Field("chapter_updatedAt")
    private Instant chapterUpdatedAt;

    @Field("chapter_publishedAt")
    private Instant chapterPublishedAt;
}
