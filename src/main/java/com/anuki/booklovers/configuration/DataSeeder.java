package com.anuki.booklovers.configuration;

import com.anuki.booklovers.models.*;
import com.anuki.booklovers.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ComicRepository comicRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        UserEntity admin = UserEntity.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .roles(new HashSet<>(Arrays.asList(Role.ADMIN)))
                .build();
        UserEntity user = UserEntity.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user123"))
                .roles(new HashSet<>(Arrays.asList(Role.USER)))
                .build();
        userRepository.saveAll(Arrays.asList(admin, user));

        List<BookEntity> books = new ArrayList<>();
        String[] bookTitles = {"1984", "To Kill a Mockingbird", "The Great Gatsby", "Pride and Prejudice", "The Catcher in the Rye"};
        String[] authors = {"George Orwell", "Harper Lee", "F. Scott Fitzgerald", "Jane Austen", "J.D. Salinger"};

        for (int i = 0; i < 5; i++) {
            BookEntity book = BookEntity.builder()
                    .title(bookTitles[i])
                    .author(authors[i])
                    .date(new Date())
                    .sinopsis("Sinopsis of " + bookTitles[i])
                    .theme(ThemeEnum.ADVENTURE)
                    .note(8.5)
                    .comments(new ArrayList<>())
                    .build();
            books.add(book);
        }
        bookRepository.saveAll(books);

        List<ComicEntity> comics = new ArrayList<>();
        String[] comicTitles = {"The Sandman", "Watchmen", "V for Vendetta", "Batman: The Killing Joke", "The Walking Dead"};
        String[] writters = {"Neil Gaiman", "Alan Moore", "Alan Moore", "Alan Moore", "Robert Kirkman"};
        String[] drawers = {"Sam Kieth", "Dave Gibbons", "David Lloyd", "Brian Bolland", "Tony Moore"};

        for (int i = 0; i < 5; i++) {
            ComicEntity comic = ComicEntity.builder()
                    .title(comicTitles[i])
                    .writter(writters[i])
                    .drawer(drawers[i])
                    .date(new Date())
                    .sinopsis("Explore the narrative of " + comicTitles[i])
                    .theme(ThemeEnum.ADVENTURE)
                    .chapters(new ArrayList<>())
                    .comments(new ArrayList<>())
                    .build();
            comics.add(comic);
        }
        comicRepository.saveAll(comics);

        List<ChapterEntity> chapters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ChapterEntity chapter = ChapterEntity.builder()
                    .title("Chapter " + (i + 1))
                    .sinopsis("Details of Chapter " + (i + 1))
                    .number(i + 1)
                    .comments(new ArrayList<>())
                    .build();
            chapters.add(chapter);
        }
        chapterRepository.saveAll(chapters);

        comics.get(0).setChapters(chapters);
        comics.get(0).getComments().add(createComment("Incredible comic!", user.getUsername(), comics.get(0), null, null));
        comics.get(1).getComments().add(createComment("Very thought-provoking!", user.getUsername(), comics.get(1), null, null));
        comicRepository.saveAll(comics);

        books.get(0).getComments().add(createComment("A must-read for everyone.", user.getUsername(), null, books.get(0), null));
        bookRepository.saveAll(books);
    }

    private CommentEntity createComment(String text, String username, ComicEntity comic, BookEntity book, ChapterEntity chapter) {
        return CommentEntity.builder()
                .comment(text)
                .date(new Date())
                .note(5)
                .userName(username)
                .comic(comic)
                .book(book)
                .chapter(chapter)
                .build();
    }
}

