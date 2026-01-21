package dev.danvega.qbe.notes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteCreateRequest {

    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    private String text;

    @NotBlank
    private String status;
}
