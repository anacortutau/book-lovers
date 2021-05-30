package com.anuki.booklovers.models;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;
    private String author;
    private String sinopsis;
    @Enumerated(EnumType.STRING)
    private ThemeEnum theme;
    @CreatedDate
    private Date date;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    private Double note;

    public Book() {
    }

    public Book(Integer id, String title, String author, String sinopsis, ThemeEnum theme, Date date, List<Comment> comments, Double note) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.sinopsis = sinopsis;
        this.theme = theme;
        this.date = date;
        this.comments = comments;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public ThemeEnum getTheme() {
        return theme;
    }

    public void setTheme(ThemeEnum theme) {
        this.theme = theme;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }
}
