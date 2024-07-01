package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(@Email
                                  @NotBlank(message = "Email cannot be blank")
                                  String email,
                                  @NotBlank(message = "Password cannot be empty")
                                  @Length(min = MIN_LENGTH, max = MAX_LENGTH)
                                  String password) {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 60;
}
