package mate.academy.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(@NotBlank(message = "name cannot be blank") String name,
                          @NotBlank(message = "description cannot be blank")
                          String description) {
}
