package com.anuki.booklovers.services;

import com.anuki.booklovers.models.Chapter;
import com.anuki.booklovers.models.Comic;
import com.anuki.booklovers.models.Comment;
import com.anuki.booklovers.repositories.ComicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComicService {

    private final ComicRepository comicRepository;

    @Transactional
    public Comic createComic(Comic comic) {
        comic.setDate(new Date());
        comic.setComments(new ArrayList<>());
        comic.setChapters(new ArrayList<>());
        comic.setNote(0.0);
        return comicRepository.save(comic);
    }

    public List<Comic> listAllComics() {
        return comicRepository.findAll();
    }

    public Optional<List<Comment>> listCommentsByComicId(Integer comicId) {
        return comicRepository.findById(comicId)
                .map(Comic::getComments);
    }

    public Optional<Comic> findComicById(Integer id) {
        return comicRepository.findById(id);
    }

    @Transactional
    public Optional<Comment> createComment(String userName, Integer comicId, Comment comment) {
        return comicRepository.findById(comicId).map(comic -> {
            comment.setUserName(userName);
            comment.setDate(new Date());
            comic.getComments().add(comment);
            updateComicNote(comic);
            comicRepository.save(comic);
            return comment;
        });
    }

    public void deleteComicById(Integer comicId) {
        comicRepository.deleteById(comicId);
    }

    public Optional<List<Chapter>> listChaptersByComicId(Integer comicId) {
        return comicRepository.findById(comicId)
                .map(Comic::getChapters);
    }

    @Transactional
    public Optional<Chapter> createChapterByComicId(Integer comicId, Chapter chapter) {
        return comicRepository.findById(comicId).map(comic -> {
            chapter.setComments(new ArrayList<>()); // Initialize empty comments list
            comic.getChapters().add(chapter);
            comicRepository.save(comic);
            return chapter;
        });
    }

    @Transactional
    public Optional<Comment> createCommentInChapter(String userName, Integer comicId, int chapterNum, Comment comment) {
        return comicRepository.findById(comicId).map(comic -> {
            for (Chapter chapter : comic.getChapters()) {
                if (chapter.getNumber() == chapterNum) {
                    comment.setUserName(userName);
                    comment.setDate(new Date());
                    chapter.getComments().add(comment);
                }
            }
            comicRepository.save(comic);
            return comment;
        });
    }

    public Optional<List<Comment>> listCommentsInChapter(Integer comicId, int chapterNum) {
        return comicRepository.findById(comicId).map(comic -> {
            for (Chapter chapter : comic.getChapters()) {
                if (chapter.getNumber() == chapterNum) {
                    return chapter.getComments();
                }
            }
            return null;
        });
    }

    private void updateComicNote(Comic comic) {
        List<Comment> comments = comic.getComments();
        double totalNote = comments.stream().mapToDouble(Comment::getNote).sum();
        comic.setNote(comments.isEmpty() ? 0 : totalNote / comments.size());
    }
}
