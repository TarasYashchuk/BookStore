package mate.academy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.validator.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "repeatPassword",
                message = "The password fields must match")
})
@Schema(description = "Request to register a new user")
public class UserRegistrationRequestDto {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    @NotBlank
    @Email
    @Schema(name = "The email of the user", required = true)
    private String email;

    @NotBlank
    @Length(min = MIN_LENGTH, max = MAX_LENGTH)
    @Schema(name = "The password of the user", required = true)
    private String password;

    @NotBlank
    @Length(min = MIN_LENGTH, max = MAX_LENGTH)
    @Schema(name = "The repeated password of the user", required = true)
    private String repeatPassword;

    @NotBlank
    @Schema(name = "The first name of the user", required = true)
    private String firstName;

    @NotBlank
    @Schema(name = "The last name of the user", required = true)
    private String lastName;

    @Schema(name = "The shipping address of the user")
    private String shippingAddress;
}
