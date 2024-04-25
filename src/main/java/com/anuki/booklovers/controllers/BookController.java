package com.anuki.booklovers.controllers;

import com.anuki.booklovers.models.BookEntity;
import com.anuki.booklovers.models.CommentEntity;
import com.anuki.booklovers.security.jwt.JwtTokenUtil;
import com.anuki.booklovers.services.BookService;
import com.anuki.booklovers.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book-lovers/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

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
    public ResponseEntity<CommentEntity> createComment(@RequestHeader("Authorization") String authorization,
                                                       @PathVariable Integer bookId,
                                                       @RequestBody CommentEntity comment) {
        UserDetails user = extractUserFromToken(authorization);
        return bookService.createComment(user, bookId, comment)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer bookId){
        bookService.deleteBookById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private UserDetails extractUserFromToken(String authorization) {
        String token = authorization.substring(7);
        log.debug("Token extraído: {}", token);
        String username = jwtTokenUtil.getUsernameFromToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado a partir del token"));
        log.debug("Nombre de usuario obtenido del token: {}", username);
        return userService.loadUserByUsername(username);
    }
}
