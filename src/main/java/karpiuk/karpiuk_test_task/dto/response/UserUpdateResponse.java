package karpiuk.karpiuk_test_task.dto.response;

import java.time.LocalDate;

public record UserUpdateResponse(
        Long id,

        String email,

        String firstName,

        String lastName,

        LocalDate birthDate,

        String address,

        String phoneNumber
) {
}
