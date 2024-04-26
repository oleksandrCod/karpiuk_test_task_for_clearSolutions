package karpiuk.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import karpiuk.karpiuk_test_task.dto.request.UserMinorUpdateRequest;
import karpiuk.karpiuk_test_task.dto.request.UserRegistrationRequest;
import karpiuk.karpiuk_test_task.dto.request.UserSearchByBirthDateRangeRequest;
import karpiuk.karpiuk_test_task.dto.request.UserUpdateAllRequest;
import karpiuk.karpiuk_test_task.dto.response.UserDeleteResponse;
import karpiuk.karpiuk_test_task.dto.response.UserRegistrationResponse;
import karpiuk.karpiuk_test_task.dto.response.UserSearchResponse;
import karpiuk.karpiuk_test_task.dto.response.UserUpdateResponse;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.DuplicateEmailException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.UserNotAdultException;
import karpiuk.karpiuk_test_task.mapper.UserMapper;
import karpiuk.karpiuk_test_task.model.User;
import karpiuk.karpiuk_test_task.repository.UserRepository;
import karpiuk.karpiuk_test_task.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest extends ServiceTestConfig {
    private static final int REQUIRED_AGE = 18;
    private static User defaultUser;
    private static UserRegistrationResponse registrationResponse;
    private static UserRegistrationRequest registrationRequest;
    private static UserRegistrationRequest invalidAgeRegistrationRequest;
    private static UserMinorUpdateRequest minorUpdateRequest;
    private static UserUpdateResponse updateResponse;
    private static UserUpdateAllRequest updateAllRequest;
    private static UserDeleteResponse deleteResponse;
    private static List<UserSearchResponse> searchResponses;
    private static UserSearchByBirthDateRangeRequest searchRequest;
    private static Long id = 1L;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Environment env;

    @Test
    @DisplayName("Verify createUser() method. Valid parameter.")
    void testCreateUser_ValidParameter_Return_UserRegistrationResponse() throws Exception {
        defaultUser = getDefaultUser();
        registrationResponse = getDefaultRegistrationResponse();
        registrationRequest = getDefaultRegistrationRequest();

        when(userRepository.save(any(User.class))).thenReturn(defaultUser);
        when(userMapper.toRegistrationResponse(defaultUser)).thenReturn(registrationResponse);

        UserRegistrationResponse actual = userService.createUser(registrationRequest);

        assertNotNull(actual);
        assertEquals(actual, registrationResponse);
    }

    @Test
    @DisplayName("Verify createUser() method. Invalid parameter age < 18.")
    void testCreateUser_InvalidAge_Return_Exception() throws Exception {
        invalidAgeRegistrationRequest = getDefaultInvalidAgeRegistrationRequest();
        userService.setRequiredAge(REQUIRED_AGE);

        UserNotAdultException ex = assertThrows(UserNotAdultException.class,
                () -> userService.createUser(invalidAgeRegistrationRequest));
    }

    @Test
    @DisplayName("Verify createUser() method. Invalid parameter duplicate email.")
    void testCreateUser_InvalidEmail_Return_Exception() throws Exception {
        registrationRequest = getDefaultRegistrationRequest();
        defaultUser = getDefaultUser();

        when(userRepository.findByEmailIgnoreCase(registrationRequest.email()))
                .thenReturn(Optional.of(defaultUser));

        DuplicateEmailException ex = assertThrows(DuplicateEmailException.class,
                () -> userService.createUser(registrationRequest));
    }

    private static UserDeleteResponse getDefaultDeleteResponse() {
        return new UserDeleteResponse("User is deleted.");
    }

    private static UserRegistrationRequest getDefaultInvalidAgeRegistrationRequest() {
        return new UserRegistrationRequest(
                "example@mail.com",
                "Bob",
                "Bobson",
                LocalDate.of(2017, 3, 3),
                "Shevchenka 7", "1234456789"
        );
    }

    private static UserRegistrationResponse getDefaultRegistrationResponse() {
        return new UserRegistrationResponse(
                "Bob",
                "Bobson",
                "example@mail.com",
                LocalDate.of(2000, 3, 3));
    }

    private static UserRegistrationRequest getDefaultRegistrationRequest() {
        return new UserRegistrationRequest(
                "example@mail.com",
                "Bob",
                "Bobson",
                LocalDate.of(2000, 3, 3),
                "Shevchenka 7", "1234456789");
    }

    private static UserMinorUpdateRequest getDefaultMinorUpdateRequest() {
        return new UserMinorUpdateRequest(
                "",
                "",
                "Bobson",
                LocalDate.of(2000, 3, 3),
                "", "1234456788");
    }

    private static UserUpdateAllRequest getDefaultUpdateAllRequest() {
        return new UserUpdateAllRequest(
                "example@mail.com",
                "Bob",
                "Bobson",
                LocalDate.of(2000, 3, 3),
                "Shevchenka 7", "1234456789"
        );
    }

    private static User getDefaultUser() {
        User user = new User();
        user.setId(id);
        user.setFirstName("Bob");
        user.setLastName("Bobson");
        user.setEmail("example@mail.com");
        user.setBirthDate(LocalDate.of(2000, 3, 3));
        user.setAddress("Shevchenka 7");
        user.setPhoneNumber("1234456789");

        return user;
    }

    private static UserUpdateResponse getDefaultUpdateResponse() {
        return new UserUpdateResponse(
                id,
                "example@mail.com",
                "Bober",
                "Bobson",
                LocalDate.of(2000, 3, 3),
                "Shevchenka 7", "1234456788");

    }
}
