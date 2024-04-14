package com.anuki.booklovers.configuration;

import com.anuki.booklovers.models.*;
import com.anuki.booklovers.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (comicRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        UserEntity admin = UserEntity.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .build();

        userRepository.save(admin);

//        ComicEntity comic = ComicEntity.builder()
//                .title("The Adventures of Captain Marvel")
//                .writter("John Smith")
//                .drawer("Jane Doe")
//                .sinopsis("Explore the universe with Captain Marvel.")
//                .theme(ThemeEnum.ADVENTURE)
//                .date(new Date())
//                .note(4.9)
//                .build();
//
//        ChapterEntity chapter1 = ChapterEntity.builder()
//                .title("First Encounter")
//                .sinopsis("Captain Marvel encounters a mysterious alien race.")
//                .number(1)
//                .build();
//
//        ChapterEntity chapter2 = ChapterEntity.builder()
//                .title("Galactic Battle")
//                .sinopsis("An epic battle between galaxies.")
//                .number(2)
//                .build();
//
//        comic.setChapters(List.of(chapter1, chapter2));
//        comicRepository.save(comic);
//
//        CommentEntity comment1 = CommentEntity.builder()
//                .comment("Amazing story!")
//                .userName("admin")
//                .date(new Date())
//                .note(5)
//                .build();
//
//        commentRepository.save(comment1);
//
//        comic.setComments(List.of(comment1));
//        comicRepository.save(comic);
    }
}

