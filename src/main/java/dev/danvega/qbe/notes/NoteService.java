package dev.danvega.qbe.notes;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "mysqlTransactionManager", readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public List<Note> search(String status, LocalDateTime from, LocalDateTime to) {
        // Route to the most specific repository method based on which filters are present.
        if (status != null && from != null && to != null) {
            return noteRepository.findByStatusIgnoreCaseAndDateTimeBetween(status, from, to);
        }
        if (status != null) {
            return noteRepository.findByStatusIgnoreCase(status);
        }
        if (from != null && to != null) {
            return noteRepository.findByDateTimeBetween(from, to);
        }
        return noteRepository.findAll();
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    public Note create(Note note) {
        // Override read-only at method level to allow writes.
        return noteRepository.save(note);
    }
}
