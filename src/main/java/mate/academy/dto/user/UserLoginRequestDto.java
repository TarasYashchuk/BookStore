package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(@Email
                                  @NotBlank(message = "Email cannot be blank")
                                  String email,
                                  @NotBlank(message = "Password cannot be empty")
                                  @Length(min = 8, max = 60)
                                  String password) {

}
