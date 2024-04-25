package com.anuki.booklovers.services;

import com.anuki.booklovers.models.BookEntity;
import com.anuki.booklovers.models.CommentEntity;
import com.anuki.booklovers.models.UserEntity;
import com.anuki.booklovers.repositories.BookRepository;
import com.anuki.booklovers.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(user.getUsername());
        if (!userEntityOpt.isPresent()) {
            log.warn("No se encontró usuario con nombre de usuario: {}", user.getUsername());
            throw new UsernameNotFoundException("Usuario no encontrado en la base de datos");
        }
        UserEntity userEntity = userEntityOpt.get();

        Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) {
            log.warn("No se encontró libro con ID: {}", bookId);
            return Optional.empty();
        }
        BookEntity book = bookOpt.get();

        comment.setUserEntity(userEntity);
        comment.setDate(new Date());
        book.getComments().add(comment);
        updateBookNoteBasedOnComments(book);
        bookRepository.save(book);
        log.debug("Comentario guardado con éxito para el libro: {}", bookId);
        return Optional.of(comment);
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
