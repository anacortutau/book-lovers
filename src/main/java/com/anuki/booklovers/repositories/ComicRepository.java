package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.Chapter;
import com.anuki.booklovers.models.Comic;
import com.anuki.booklovers.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, Integer> {
    public List<Comment> findCommentsById(Integer comicId);
    public List<Chapter> findChaptersById(Integer comicId);
}
