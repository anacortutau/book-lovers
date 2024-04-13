package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
