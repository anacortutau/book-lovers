package com.anuki.booklovers.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "title", "author"})
public class ComicList {

    private Integer id;
    private String title;
    private String author;
    private Double note;
}
