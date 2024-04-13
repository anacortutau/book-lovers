package com.anuki.booklovers.models;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "title", "sinopsis", "number"})
public class Chapter implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String title;
    private String sinopsis;
    private int number;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
}
