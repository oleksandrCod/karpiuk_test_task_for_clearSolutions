package karpiuk.karpiuk_test_task.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UserRegistrationRequest(
        @Email
        @NotBlank(message = UserRegistrationRequestMessages.EMAIL_EMPTY)
        String email,

        @NotBlank(message = UserRegistrationRequestMessages.FIRST_NAME_EMPTY)
        String firstName,

        @NotBlank(message = UserRegistrationRequestMessages.LAST_NAME_EMPTY)
        String lastName,

        @Past
        @NotNull(message = UserRegistrationRequestMessages.BIRTH_DATE_EMPTY)
        LocalDate birthDate,


        String address,

        @Size(min = 6, max = 10)
        String phoneNumber) {

    public static class UserRegistrationRequestMessages {
        public static final String EMAIL_EMPTY = "Please enter your email, field can't be empty.";
        public static final String FIRST_NAME_EMPTY = "Please enter your name, field can't be empty.";
        public static final String LAST_NAME_EMPTY = "Please enter your last name, field can't be empty.";
        public static final String BIRTH_DATE_EMPTY = "Please enter your birth date, field can't be empty.";
    }
}
