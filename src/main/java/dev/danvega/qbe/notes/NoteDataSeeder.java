package dev.danvega.qbe.notes;

import java.time.LocalDateTime;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NoteDataSeeder implements ApplicationRunner {

    private final NoteRepository noteRepository;

    public NoteDataSeeder(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public void run(ApplicationArguments args) {
        if (noteRepository.count() > 0) {
            return;
        }

        noteRepository.save(new Note(
                LocalDateTime.now().minusDays(1),
                "First note",
                "open"
        ));
        noteRepository.save(new Note(
                LocalDateTime.now(),
                "Second note",
                "done"
        ));
    }
}
