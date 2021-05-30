package com.anuki.booklovers.services;

import com.anuki.booklovers.models.*;
import com.anuki.booklovers.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComicService {

    @Autowired
    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public Comic createComic(Comic comic) {
        comic.setComments(new ArrayList<Comment>());
        comic.setChapters(new ArrayList<Chapter>());
        comic.setDate(Date.from(Instant.now()));
        comic.setNote((double)0);
        return comicRepository.save(comic);
    }

    public List<Comic> list() {
        return comicRepository.findAll();
    }

    public List<Comment> listCommentsByComicId(String comicId) {
        return comicRepository.findCommentsById(comicId);
    }

    public Comment createComment(String userName, String comicId, Comment comment) {
        comment.setUserName(userName);
        comment.setDate(Date.from(Instant.now()));
        Comic comic = comicRepository.findById(comicId).get();
        List<Comment> comments = comic.getComments();
        double accumulator = (double) 0;
        for(Comment commet : comments){
            accumulator =+ (double) commet.getNote();
        }
        accumulator = accumulator/comments.size();
        comic.setNote(accumulator);
        comic.getComments().add(comment);
        comicRepository.save(comic);
        return comment;
    }

    public void deleteBookById(String comicId) {
        comicRepository.deleteById(comicId);
    }

    public List<Chapter> listChaptersByComicId(String comicId) {
        return comicRepository.findChaptersById(comicId);
    }

    public Chapter createChapterByComicId(String userName, String comicId, Chapter chapter) {
        chapter.setComments(new ArrayList<Comment>());
        Comic comic = comicRepository.findById(comicId).get();
        comic.getChapters().add(chapter);
        comicRepository.save(comic);
        return chapter;
    }

    public Comment createCommentInChapter(String userName, String comicId, int chapterNum, Comment comment) {
        comment.setUserName(userName);
        comment.setDate(Date.from(Instant.now()));
        Comic comic = comicRepository.findById(comicId).get();
        List<Chapter> chapters = comic.getChapters();
        List<Chapter> newChapters = new ArrayList<Chapter>();
        for (Chapter chapter:chapters) {
            if(chapter.getNumber() == chapterNum) {
                List<Comment> comments = chapter.getComments();
                comments.add(comment);
                chapter.setComments(comments);
            }
            newChapters.addAll(chapters);
        }
        comic.setChapters(newChapters);
        comicRepository.save(comic);
        return comment;
    }

    public List<Comment> listCommentsInChapter(String comicId, int chapterNum) {
        List<Chapter> chapters = comicRepository.findChaptersById(comicId);
        List<Comment> comments = new ArrayList<Comment>();
        for (Chapter chapter:chapters) {
            if(chapter.getNumber() == chapterNum){
                comments = chapter.getComments();
            }
        }
        return comments;
    }
}
