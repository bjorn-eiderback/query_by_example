package dev.danvega.qbe.notes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByStatusIgnoreCase(String status);

    List<Note> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Note> findByStatusIgnoreCaseAndDateTimeBetween(String status, LocalDateTime from, LocalDateTime to);
}
