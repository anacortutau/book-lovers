package com.anuki.booklovers.repositories;

import com.anuki.booklovers.models.Comic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComicRepository extends JpaRepository<Comic, Integer> {
}
