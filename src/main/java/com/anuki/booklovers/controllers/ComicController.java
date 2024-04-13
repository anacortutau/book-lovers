package com.anuki.booklovers.controllers;

import com.anuki.booklovers.models.Chapter;
import com.anuki.booklovers.models.Comic;
import com.anuki.booklovers.models.Comment;
import com.anuki.booklovers.services.ComicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book-lovers/comics")
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;

    @PostMapping
    public ResponseEntity<?> createComic(@RequestBody Comic comic) {
        Comic createdComic = comicService.createComic(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComic);
    }

    @GetMapping
    public ResponseEntity<List<Comic>> listComics() {
        List<Comic> comics = comicService.listAllComics();
        return ResponseEntity.ok(comics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComic(@PathVariable Integer id) {
        return comicService.findComicById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{comicId}/comments")
    public ResponseEntity<?> listComments(@PathVariable Integer comicId) {
        return comicService.listCommentsByComicId(comicId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{comicId}/comments")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal UserDetails user,
                                           @PathVariable Integer comicId,
                                           @RequestBody Comment comment) {
        return comicService.createComment(user.getUsername(), comicId, comment)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{comicId}/chapters")
    public ResponseEntity<?> listChapters(@PathVariable Integer comicId) {
        return comicService.listChaptersByComicId(comicId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{comicId}/chapters")
    public ResponseEntity<?> createChapter(@PathVariable Integer comicId,
                                           @RequestBody Chapter chapter) {
        return comicService.createChapterByComicId(comicId, chapter)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{comicId}/chapters/{chapterNum}/comments")
    public ResponseEntity<?> createCommentInChapter(@AuthenticationPrincipal UserDetails user,
                                                    @PathVariable Integer comicId,
                                                    @PathVariable int chapterNum,
                                                    @RequestBody Comment comment) {
        return comicService.createCommentInChapter(user.getUsername(), comicId, chapterNum, comment)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{comicId}/chapters/{chapterNum}/comments")
    public ResponseEntity<?> listCommentsInChapter(@PathVariable Integer comicId,
                                                   @PathVariable int chapterNum) {
        return comicService.listCommentsInChapter(comicId, chapterNum)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{comicId}")
    public ResponseEntity<Void> deleteComic(@PathVariable Integer comicId) {
        comicService.deleteComicById(comicId);
        return ResponseEntity.ok().build();
    }
}

