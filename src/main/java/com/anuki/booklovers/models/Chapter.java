package com.anuki.booklovers.models;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Chapter implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String title;
    private String sinopsis;
    private int number;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Chapter() {
    }

    public Chapter(String title, String sinopsis, int number, List<Comment> comments) {
        this.title = title;
        this.sinopsis = sinopsis;
        this.number = number;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
