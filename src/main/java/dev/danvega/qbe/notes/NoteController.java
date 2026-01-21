package dev.danvega.qbe.notes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> findAll() {
        return ResponseEntity.ok(noteService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Note>> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(noteService.search(status, from, to));
    }

    @PostMapping
    public ResponseEntity<Note> create(@Valid @RequestBody NoteCreateRequest request) {
        Note note = Note.builder()
                .dateTime(request.dateTime())
                .text(request.text())
                .status(request.status())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.create(note));
    }
}
