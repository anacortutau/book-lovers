package com.anuki.booklovers.models;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Comic implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    private String title;
    private String writter;
    private String drawer;
    private String sinopsis;
    @Enumerated(EnumType.STRING)
    private ThemeEnum theme;
    @CreatedDate
    private Date date;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Chapter> chapters;
    private Double note;

    public Comic() {
    }

    public Comic(String id, String title, String writter, String drawer, String sinopsis, ThemeEnum theme, Date date, List<Comment> comments, List<Chapter> chapters, Double note) {
        this.id = id;
        this.title = title;
        this.writter = writter;
        this.drawer = drawer;
        this.sinopsis = sinopsis;
        this.theme = theme;
        this.date = date;
        this.comments = comments;
        this.chapters = chapters;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWritter() {
        return writter;
    }

    public void setWritter(String writter) {
        this.writter = writter;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
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

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }
}
