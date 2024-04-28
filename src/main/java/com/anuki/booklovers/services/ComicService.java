package com.anuki.booklovers.services;

import com.anuki.booklovers.models.*;
import com.anuki.booklovers.repositories.ComicRepository;
import com.anuki.booklovers.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComicService {

    private final ComicRepository comicRepository;
    private final UserRepository userRepository;

    @Transactional
    public ComicEntity createComic(ComicEntity comic) {
        comic.setDate(new Date());
        comic.setComments(new ArrayList<>());
        comic.setChapters(new ArrayList<>());
        comic.setNote(0.0);
        return comicRepository.save(comic);
    }

    public List<ComicList> listAllComics() {
        List<ComicList> comicLists = new ArrayList<>();

        comicRepository.findAll().forEach(comicEntity -> {
            ComicList comicList = new ComicList();

            comicList.setTitle(comicEntity.getTitle());
            comicList.setId(comicEntity.getId());
            comicList.setAuthor(comicEntity.getWritter());
            comicList.setNote(comicEntity.getNote());

            comicLists.add(comicList);
        });

        return comicLists;
    }

    public Optional<List<CommentEntity>> listCommentsByComicId(Integer comicId) {
        return comicRepository.findById(comicId)
                .map(ComicEntity::getComments);
    }

    public Optional<ComicEntity> findComicById(Integer id) {
        return comicRepository.findById(id);
    }

    @Transactional
    public Optional<CommentEntity> createComment(UserDetails user, Integer bookId, CommentEntity comment) {
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(user.getUsername());
        if (!userEntityOpt.isPresent()) {
            log.warn("No se encontró usuario con nombre de usuario: {}", user.getUsername());
            throw new UsernameNotFoundException("Usuario no encontrado en la base de datos");
        }
        UserEntity userEntity = userEntityOpt.get();

        Optional<ComicEntity> comicOpt = comicRepository.findById(bookId);
        if (!comicOpt.isPresent()) {
            log.warn("No se encontró libro con ID: {}", bookId);
            return Optional.empty();
        }
        ComicEntity comic = comicOpt.get();

        comment.setUserEntity(userEntity);
        comment.setDate(new Date());
        comic.getComments().add(comment);
        updateComicNoteBasedOnComments(comic);
        comicRepository.save(comic);
        log.debug("Comentario guardado con éxito para el comic: {}", bookId);
        return Optional.of(comment);
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
    public Optional<CommentEntity> createCommentInChapter(UserDetails userDetails, Integer comicId, int chapterNum, CommentEntity comment) {
        UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername()).get();

        return comicRepository.findById(comicId).map(comic -> {
            for (ChapterEntity chapter : comic.getChapters()) {
                if (chapter.getNumber() == chapterNum) {
                    comment.setUserEntity(userEntity);
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

    private void updateComicNoteBasedOnComments(ComicEntity comic) {
        List<CommentEntity> comments = comic.getComments();
        double totalNote = comments.stream().mapToDouble(CommentEntity::getNote).sum();
        double averageNote = totalNote / comments.size();
        comic.setNote(averageNote);
    }
}
