package mate.academy.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response containing user details")
public class UserResponseDto {
    @Schema(name = "The unique ID of the user")
    private Long id;

    @Schema(name = "The email of the user")
    private String email;

    @Schema(name = "The first name of the user")
    private String firstName;

    @Schema(name = "The last name of the user")
    private String lastName;

    @Schema(name = "The shipping address of the user")
    private String shippingAddress;
}
