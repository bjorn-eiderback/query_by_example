package dev.danvega.qbe.notes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record NoteCreateRequest(
        @NotNull LocalDateTime dateTime,
        @NotBlank String text,
        @NotBlank String status
) {
}
