package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.Book;
import com.anuki.booklovers.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    public List<Comment> findCommentsById(Integer id);
}
