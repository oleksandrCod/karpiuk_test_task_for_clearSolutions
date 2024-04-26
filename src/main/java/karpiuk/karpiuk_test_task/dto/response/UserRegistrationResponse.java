package karpiuk.karpiuk_test_task.dto.response;

import java.time.LocalDate;

public record UserRegistrationResponse(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate) {
}
