package com.anuki.booklovers.controllers;

import com.anuki.booklovers.models.Chapter;
import com.anuki.booklovers.models.Comic;
import com.anuki.booklovers.models.Comment;
import com.anuki.booklovers.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book-lovers/comics")
public class ComicController {

    @Autowired
    private final ComicService comicService;

    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    @PostMapping
    public ResponseEntity<Comic> createComic(@RequestBody Comic comic){
        return new ResponseEntity<Comic>(comicService.createComic(comic), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Comic>> listComic(){
        return new ResponseEntity<List<Comic>>(comicService.list(), HttpStatus.OK);
    }

    @GetMapping("/{comicId}/comments")
    public ResponseEntity<List<Comment>> listComments(@PathVariable String comicId){
        return new ResponseEntity<List<Comment>>(comicService.listCommentsByComicId(comicId), HttpStatus.OK);
    }

    @PostMapping("/{comicId}/comments")
    public ResponseEntity<Comment> createComment(@AuthenticationPrincipal UserDetails user,
                                                 @PathVariable String comicId,
                                                 @RequestBody Comment comment){

        return new ResponseEntity<Comment> (comicService.createComment(user.getUsername(), comicId, comment),HttpStatus.CREATED);

    }

    @GetMapping("/{comicId}/chapters")
    public ResponseEntity<List<Chapter>> listChapters(@PathVariable String comicId){
        return new ResponseEntity<List<Chapter>> (comicService.listChaptersByComicId(comicId), HttpStatus.OK);
    }

    @PostMapping("/{comicId}/chapters/{chapterNum}/comments")
    public ResponseEntity<Comment> createCommentInChapter(@AuthenticationPrincipal UserDetails user,
                                                  @PathVariable String comicId,
                                                  @PathVariable int chapterNum,
                                                  @RequestBody Comment comment) {
        return new ResponseEntity<Comment> (comicService.createCommentInChapter(user.getUsername(), comicId, chapterNum, comment), HttpStatus.CREATED);
    }

    @GetMapping("/{comicId}/chapters/{chapterNum}/comments")
    public ResponseEntity<List<Comment>> listCommentsInChapter(@PathVariable String comicId,
                                                               @PathVariable int chapterNum) {
        return new ResponseEntity<List<Comment>> (comicService.listCommentsInChapter(comicId, chapterNum), HttpStatus.OK);
    }

    @PostMapping("/{comicId}/chapters")
    public ResponseEntity<Chapter> createChapters(@AuthenticationPrincipal UserDetails user,
                                                  @PathVariable String comicId,
                                                  @RequestBody Chapter chapter) {
        return new ResponseEntity<Chapter> (comicService.createChapterByComicId(user.getUsername(), comicId, chapter), HttpStatus.OK);
    }

    @DeleteMapping("/{comicId}")
    public ResponseEntity<?> deleteComic(@PathVariable String comicId){
        comicService.deleteBookById(comicId);
        return new ResponseEntity<> (HttpStatus.OK);
    }


}
