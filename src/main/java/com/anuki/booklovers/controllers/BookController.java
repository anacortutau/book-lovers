package com.anuki.booklovers.controllers;

import com.anuki.booklovers.models.Book;
import com.anuki.booklovers.models.Comment;
import com.anuki.booklovers.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book-lovers/books")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        return new ResponseEntity<Book>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> listBooks(){
        return new ResponseEntity<List<Book>>(bookService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id){
        return new ResponseEntity<Book>(bookService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> listComments(@PathVariable Integer id){
        return new ResponseEntity<List<Comment>>(bookService.listCommentsByBookId(id), HttpStatus.OK);
    }

    @PostMapping("/{bookId}/comments")
    public ResponseEntity<Comment> createComment(@AuthenticationPrincipal UserDetails user,
                                                 @PathVariable Integer bookId,
                                                 @RequestBody Comment comment) {

        return new ResponseEntity<Comment>(bookService.createComment(user.getUsername(), bookId, comment),HttpStatus.CREATED);

    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer bookId){
        bookService.deleteBookById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
