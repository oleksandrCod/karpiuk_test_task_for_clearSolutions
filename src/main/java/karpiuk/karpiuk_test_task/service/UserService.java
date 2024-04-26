package karpiuk.karpiuk_test_task.service;

import java.util.List;
import karpiuk.karpiuk_test_task.dto.request.UserMinorUpdateRequest;
import karpiuk.karpiuk_test_task.dto.request.UserRegistrationRequest;
import karpiuk.karpiuk_test_task.dto.request.UserSearchByBirthDateRangeRequest;
import karpiuk.karpiuk_test_task.dto.request.UserUpdateAllRequest;
import karpiuk.karpiuk_test_task.dto.response.UserDeleteResponse;
import karpiuk.karpiuk_test_task.dto.response.UserRegistrationResponse;
import karpiuk.karpiuk_test_task.dto.response.UserSearchResponse;
import karpiuk.karpiuk_test_task.dto.response.UserUpdateResponse;

public interface UserService {
    UserRegistrationResponse createUser(UserRegistrationRequest request);

    UserUpdateResponse minorUpdate(UserMinorUpdateRequest request, Long id);

    UserUpdateResponse updateAll(UserUpdateAllRequest request, Long id);

    UserDeleteResponse deleteById(Long id);

    List<UserSearchResponse> findByBirthDateRange(UserSearchByBirthDateRangeRequest request);
}
