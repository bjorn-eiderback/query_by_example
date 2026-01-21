package dev.danvega.qbe.notes;

import dev.danvega.qbe.DatabaseTestContainers;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NoteRepositoryTest extends DatabaseTestContainers {

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        noteRepository.deleteAll();

        noteRepository.save(Note.builder()
                .dateTime(LocalDateTime.of(2024, 1, 1, 10, 0))
                .text("First note")
                .status("open")
                .build());
        noteRepository.save(Note.builder()
                .dateTime(LocalDateTime.of(2024, 1, 2, 12, 30))
                .text("Second note")
                .status("done")
                .build());
        noteRepository.save(Note.builder()
                .dateTime(LocalDateTime.of(2024, 1, 3, 9, 15))
                .text("Third note")
                .status("open")
                .build());
    }

    @Test
    void findByStatusIgnoreCase() {
        List<Note> results = noteRepository.findByStatusIgnoreCase("OPEN");

        assertThat(results)
                .hasSize(2)
                .allMatch(note -> "open".equalsIgnoreCase(note.getStatus()));
    }

    @Test
    void findByDateTimeBetween() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 2, 23, 59);

        List<Note> results = noteRepository.findByDateTimeBetween(from, to);

        assertThat(results)
                .hasSize(2)
                .allMatch(note -> !note.getDateTime().isBefore(from) && !note.getDateTime().isAfter(to));
    }

    @Test
    void findByStatusAndDateTimeBetween() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 3, 0, 0);

        List<Note> results = noteRepository
                .findByStatusIgnoreCaseAndDateTimeBetween("open", from, to);

        assertThat(results)
                .hasSize(1)
                .allMatch(note -> "open".equalsIgnoreCase(note.getStatus()));
    }
}
