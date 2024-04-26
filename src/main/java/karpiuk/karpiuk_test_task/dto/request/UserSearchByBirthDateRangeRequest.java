package karpiuk.karpiuk_test_task.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record UserSearchByBirthDateRangeRequest(
        @NotNull
        @PastOrPresent
        LocalDate from,

        @NotNull
        @PastOrPresent
        LocalDate to) {
}
