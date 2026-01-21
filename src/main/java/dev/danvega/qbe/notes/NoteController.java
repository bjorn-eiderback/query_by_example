package dev.danvega.qbe.notes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Create and search notes stored in MySQL")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "List all notes")
    public ResponseEntity<List<Note>> findAll() {
        return ResponseEntity.ok(noteService.findAll());
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search notes with optional filters",
            description = "Filters by status and/or date-time range when provided."
    )
    public ResponseEntity<List<Note>> search(
            @Parameter(description = "Case-insensitive status match")
            @RequestParam(required = false) String status,
            @Parameter(description = "Start of ISO-8601 date-time range")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "End of ISO-8601 date-time range")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(noteService.search(status, from, to));
    }

    @PostMapping
    @Operation(summary = "Create a new note")
    public ResponseEntity<Note> create(@Valid @RequestBody NoteCreateRequest request) {
        Note note = Note.builder()
                .dateTime(request.dateTime())
                .text(request.text())
                .status(request.status())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.create(note));
    }
}
