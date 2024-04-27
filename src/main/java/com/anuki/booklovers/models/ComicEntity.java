package com.anuki.booklovers.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "title", "writter", "drawer", "theme"})
public class ComicEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String writter;
    private String drawer;
    private String sinopsis;

    @Enumerated(EnumType.STRING)
    private ThemeEnum theme;

    @CreatedDate
    private Date date;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comic_entity_id")
    @JsonManagedReference
    private List<CommentEntity> comments;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comic_entity_id")
    @JsonManagedReference
    private List<ChapterEntity> chapters;

    private Double note;
}