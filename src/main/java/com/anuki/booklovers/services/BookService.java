package com.anuki.booklovers.services;

import com.anuki.booklovers.models.BookEntity;
import com.anuki.booklovers.models.CommentEntity;
import com.anuki.booklovers.models.UserEntity;
import com.anuki.booklovers.repositories.BookRepository;
import com.anuki.booklovers.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;

    @Transactional
    public BookEntity createBook(BookEntity book) {
        book.setDate(new Date());
        book.setComments(new ArrayList<>());
        book.setNote(0.0);
        return bookRepository.save(book);
    }

    public List<BookEntity> listAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<List<CommentEntity>> listCommentsByBookId(Integer id) {
        return bookRepository.findById(id).map(BookEntity::getComments);
    }

    public Optional<BookEntity> findBookById(Integer id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Optional<CommentEntity> createComment(UserDetails user, Integer bookId, CommentEntity comment) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername()).get();

        return bookRepository.findById(bookId).map(book -> {
            comment.setUserEntity(userEntity);
            comment.setDate(new Date());
            book.getComments().add(comment);
            updateBookNoteBasedOnComments(book);
            bookRepository.save(book);
            return comment;
        });
    }

    private void updateBookNoteBasedOnComments(BookEntity book) {
        List<CommentEntity> comments = book.getComments();
        double totalNote = comments.stream().mapToDouble(CommentEntity::getNote).sum();
        double averageNote = totalNote / comments.size();
        book.setNote(averageNote);
    }

    public void deleteBookById(Integer bookId) {
        bookRepository.deleteById(bookId);
    }
}
