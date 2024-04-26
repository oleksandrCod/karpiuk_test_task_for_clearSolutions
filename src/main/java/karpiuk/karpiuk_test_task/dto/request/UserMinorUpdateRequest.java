package karpiuk.karpiuk_test_task.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UserMinorUpdateRequest(
        @Email
        String email,

        String firstName,

        String lastName,

        @Past
        LocalDate birthDate,

        String address,

        @Size(min = 6, max = 10)
        String phoneNumber
) {
}
