package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.Chapter;
import com.anuki.booklovers.models.Comic;
import com.anuki.booklovers.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, String> {
    public List<Comment> findCommentsById(String comicId);
    public List<Chapter> findChaptersById(String comicId);
}
