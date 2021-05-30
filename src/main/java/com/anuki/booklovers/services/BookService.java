package com.anuki.booklovers.services;

import com.anuki.booklovers.models.Book;
import com.anuki.booklovers.models.Comment;
import com.anuki.booklovers.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        book.setDate(Date.from(Instant.now()));
        book.setComments(new ArrayList<Comment>());
        book.setNote((double) 0);
        return bookRepository.save(book);
    }

    public List<Book> list() {
        return bookRepository.findAll();
    }

    public List<Comment> listCommentsByBookId(Integer id) {
        Book book = bookRepository.findById(id).get();
        return book.getComments();
    }

    public Book findById(Integer id) {
        return bookRepository.findById(id).get();
    }

    public Comment createComment(String userName, Integer bookId, Comment comment) {
        comment.setUserName(userName);
        comment.setDate(Date.from(Instant.now()));
        Book book = bookRepository.findById(bookId).get();
        List<Comment> comments = book.getComments();
        comments.add(comment);
        double accumulator = 0;
        for(Comment commet : comments){
           accumulator = accumulator + (double) commet.getNote();
        }
        accumulator = accumulator/comments.size();
        book.setNote(accumulator);
        bookRepository.save(book);
        return comment;
    }

    public void deleteBookById(Integer bookId) {
        bookRepository.deleteById(bookId);
    }
}
