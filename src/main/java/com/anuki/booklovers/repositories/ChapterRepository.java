package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
}
