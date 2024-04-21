package com.anuki.booklovers.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class CommentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;
    private String comment;

    @CreatedDate
    private Date date;

    private int note;

    @ManyToOne
    @JoinColumn(name = "book_entity_id")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "chapter_entity_id")
    private ChapterEntity chapter;

    @ManyToOne
    @JoinColumn(name = "comic_entity_id")
    private ComicEntity comic;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}