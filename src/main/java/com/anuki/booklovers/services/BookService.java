package com.anuki.booklovers.services;

import com.anuki.booklovers.models.Book;
import com.anuki.booklovers.models.Comment;
import com.anuki.booklovers.repositories.BookRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book createBook(Book book) {
        book.setDate(new Date());
        book.setComments(new ArrayList<>());
        book.setNote(0.0);
        return bookRepository.save(book);
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<List<Comment>> listCommentsByBookId(Integer id) {
        return bookRepository.findById(id).map(Book::getComments);
    }

    public Optional<Book> findBookById(Integer id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Optional<Comment> createComment(String userName, Integer bookId, Comment comment) {
        return bookRepository.findById(bookId).map(book -> {
            comment.setUserName(userName);
            comment.setDate(new Date());
            book.getComments().add(comment);
            updateBookNoteBasedOnComments(book);
            bookRepository.save(book);
            return comment;
        });
    }

    private void updateBookNoteBasedOnComments(Book book) {
        List<Comment> comments = book.getComments();
        double totalNote = comments.stream().mapToDouble(Comment::getNote).sum();
        double averageNote = totalNote / comments.size();
        book.setNote(averageNote);
    }

    public void deleteBookById(Integer bookId) {
        bookRepository.deleteById(bookId);
    }
}
