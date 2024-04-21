package com.anuki.booklovers.controllers;

import com.anuki.booklovers.models.BookEntity;
import com.anuki.booklovers.models.CommentEntity;
import com.anuki.booklovers.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book-lovers/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity book){
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookEntity>> listBooks(){
        return new ResponseEntity<>(bookService.listAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBook(@PathVariable Integer id){
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentEntity>> listComments(@PathVariable Integer id){
        return bookService.listCommentsByBookId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{bookId}/comments")
    public ResponseEntity<CommentEntity> createComment(@AuthenticationPrincipal UserDetails user,
                                                       @PathVariable Integer bookId,
                                                       @RequestBody CommentEntity comment) {

        return bookService.createComment(user, bookId, comment)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer bookId){
        bookService.deleteBookById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
