package com.anuki.booklovers.services;

import com.anuki.booklovers.models.ChapterEntity;
import com.anuki.booklovers.models.ComicEntity;
import com.anuki.booklovers.models.CommentEntity;
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
    public ComicEntity createComic(ComicEntity comic) {
        comic.setDate(new Date());
        comic.setComments(new ArrayList<>());
        comic.setChapters(new ArrayList<>());
        comic.setNote(0.0);
        return comicRepository.save(comic);
    }

    public List<ComicEntity> listAllComics() {
        return comicRepository.findAll();
    }

    public Optional<List<CommentEntity>> listCommentsByComicId(Integer comicId) {
        return comicRepository.findById(comicId)
                .map(ComicEntity::getComments);
    }

    public Optional<ComicEntity> findComicById(Integer id) {
        return comicRepository.findById(id);
    }

    @Transactional
    public Optional<CommentEntity> createComment(String userName, Integer comicId, CommentEntity comment) {
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

    public Optional<List<ChapterEntity>> listChaptersByComicId(Integer comicId) {
        return comicRepository.findById(comicId)
                .map(ComicEntity::getChapters);
    }

    @Transactional
    public Optional<ChapterEntity> createChapterByComicId(Integer comicId, ChapterEntity chapter) {
        return comicRepository.findById(comicId).map(comic -> {
            chapter.setComments(new ArrayList<>()); // Initialize empty comments list
            comic.getChapters().add(chapter);
            comicRepository.save(comic);
            return chapter;
        });
    }

    @Transactional
    public Optional<CommentEntity> createCommentInChapter(String userName, Integer comicId, int chapterNum, CommentEntity comment) {
        return comicRepository.findById(comicId).map(comic -> {
            for (ChapterEntity chapter : comic.getChapters()) {
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

    public Optional<List<CommentEntity>> listCommentsInChapter(Integer comicId, int chapterNum) {
        return comicRepository.findById(comicId).map(comic -> {
            for (ChapterEntity chapter : comic.getChapters()) {
                if (chapter.getNumber() == chapterNum) {
                    return chapter.getComments();
                }
            }
            return null;
        });
    }

    private void updateComicNote(ComicEntity comic) {
        List<CommentEntity> comments = comic.getComments();
        double totalNote = comments.stream().mapToDouble(CommentEntity::getNote).sum();
        comic.setNote(comments.isEmpty() ? 0 : totalNote / comments.size());
    }
}
