package com.anuki.booklovers.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "title", "sinopsis", "number"})
public class ChapterEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;
    private String sinopsis;
    private int number;

    @ManyToOne
    @JoinColumn(name = "comic_entity_id")
    private ComicEntity comic;
}
