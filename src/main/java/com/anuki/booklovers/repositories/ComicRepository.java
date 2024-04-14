package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.ComicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends JpaRepository<ComicEntity, Integer> {
}
