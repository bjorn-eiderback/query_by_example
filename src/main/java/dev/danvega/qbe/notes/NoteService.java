package dev.danvega.qbe.notes;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = "mysqlTransactionManager", readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public List<Note> search(String status, LocalDateTime from, LocalDateTime to) {
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
        return noteRepository.save(note);
    }
}
