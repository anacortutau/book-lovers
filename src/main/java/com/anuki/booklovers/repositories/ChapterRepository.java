package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Integer> {
}
