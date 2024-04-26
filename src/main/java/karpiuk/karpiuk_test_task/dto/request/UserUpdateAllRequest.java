package karpiuk.karpiuk_test_task.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UserUpdateAllRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Past
        @NotNull
        LocalDate birthDate,

        @NotBlank
        String address,

        @NotBlank
        @Size(min = 6, max = 10)
        String phoneNumber) {
}
