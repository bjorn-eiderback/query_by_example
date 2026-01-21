package dev.danvega.qbe.notes;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NoteDataSeeder implements ApplicationRunner {

    private final NoteRepository noteRepository;

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public void run(ApplicationArguments args) {
        if (noteRepository.count() > 0) {
            return;
        }

        noteRepository.save(Note.builder()
                .dateTime(LocalDateTime.now().minusDays(1))
                .text("First note")
                .status("open")
                .build());
        noteRepository.save(Note.builder()
                .dateTime(LocalDateTime.now())
                .text("Second note")
                .status("done")
                .build());
    }
}
