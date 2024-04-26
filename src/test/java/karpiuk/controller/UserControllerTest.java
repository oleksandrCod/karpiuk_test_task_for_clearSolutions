package karpiuk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import karpiuk.karpiuk_test_task.KarpiukTestTaskApplication;
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
import karpiuk.karpiuk_test_task.repository.UserRepository;
import karpiuk.karpiuk_test_task.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = KarpiukTestTaskApplication.class)
public class UserControllerTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;
    private static UserRegistrationResponse registrationResponse;
    private static UserRegistrationRequest registrationRequest;
    private static UserMinorUpdateRequest minorUpdateRequest;
    private static UserUpdateResponse updateResponse;
    private static UserUpdateAllRequest updateAllRequest;
    private static UserDeleteResponse deleteResponse;
    private static List<UserSearchResponse> searchResponses;
    private static UserSearchByBirthDateRangeRequest searchRequest;
    private static Long id = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("Verify register() method. Valid request")
    void registerUser_validRequest_Return_registerResponse() throws Exception {
        registrationResponse = getDefaultRegistrationResponse();
        registrationRequest = getDefaultRegistrationRequest();

        when(userService.createUser(any(UserRegistrationRequest.class)))
                .thenReturn(registrationResponse);

        String requestContent = objectMapper.writeValueAsString(registrationRequest);
        String expected = objectMapper.writeValueAsString(registrationResponse);

        this.mockMvc.perform(post("/users/register")
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(content().json(expected));
    }

    @Test
    @DisplayName("Verify register() method. Invalid request, duplicate email.")
    void registerUser_duplicatedEmail_Return_ErrorResponse() throws Exception {
        registrationRequest = getDefaultRegistrationRequest();

        when(userService.createUser(any(UserRegistrationRequest.class)))
                .thenThrow(DuplicateEmailException.class);

        String content = objectMapper.writeValueAsString(registrationRequest);

        this.mockMvc.perform(post("/users/register")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Verify register() method. Invalid request, user not adult.")
    void registerUser_NotAdult_Return_ErrorResponse() throws Exception {
        registrationRequest = getDefaultRegistrationRequest();

        String content = objectMapper.writeValueAsString(registrationRequest);

        when(userService.createUser(any(UserRegistrationRequest.class)))
                .thenThrow(UserNotAdultException.class);

        this.mockMvc.perform(post("/users/register")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Verify minorUpdate() method. Valid request.")
    void minorUpdateUser_validRequest_Return_updateResponse() throws Exception {
        updateResponse = getDefaultUpdateResponse();
        minorUpdateRequest = getDefaultMinorUpdateRequest();

        String content = objectMapper.writeValueAsString(minorUpdateRequest);
        String expected = objectMapper.writeValueAsString(updateResponse);

        when(userService.minorUpdate(minorUpdateRequest, id)).thenReturn(updateResponse);

        this.mockMvc.perform(patch("/users/minor-update/{id}", id)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andExpect(content().json(expected));

    }

    @Test
    @DisplayName("Verify fullUpdate() method. Valid request.")
    void fullUpdateUser_validRequest_Return_updateResponse() throws Exception {
        updateResponse = getDefaultUpdateResponse();
        updateAllRequest = getDefaultUpdateAllRequest();

        String content = objectMapper.writeValueAsString(updateAllRequest);
        String expected = objectMapper.writeValueAsString(updateResponse);

        when(userService.updateAll(updateAllRequest, id)).thenReturn(updateResponse);

        this.mockMvc.perform(put("/users/update-all/{id}", id)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andExpect(content().json(expected));
    }

    @Test
    @DisplayName("Verify delete() method. Valid request.")
    void deleteUser_ById_ValidRequest_Return_deleteResponse() throws Exception {
        deleteResponse = getDefaultDeleteResponse();

        when(userService.deleteById(id)).thenReturn(deleteResponse);

        String expected = objectMapper.writeValueAsString(deleteResponse);

        this.mockMvc.perform(delete("/users/delete/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andExpect(content().json(expected));
    }

    @Test
    @DisplayName("Verify findAllByBirthDateRange() method. Valid request.")
    void findAllByBirthDateRange_ValidRequest_Return_UserSearchResponseList() throws Exception {
        searchResponses = getDefaultSearchResponseList();
        searchRequest = getDefaultSearchRequest();

        String content = objectMapper.writeValueAsString(searchRequest);
        String expected = objectMapper.writeValueAsString(searchResponses);

        when(userService.findByBirthDateRange(searchRequest)).thenReturn(searchResponses);

        this.mockMvc.perform(get("/users/search")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound()).andExpect(content().json(expected));

    }

    private static UserSearchByBirthDateRangeRequest getDefaultSearchRequest() {
        return new UserSearchByBirthDateRangeRequest(LocalDate.of(2000, 1, 1), LocalDate.of(2024, 1, 1));
    }

    private static List<UserSearchResponse> getDefaultSearchResponseList() {
        return List.of(new UserSearchResponse(
                id,
                "example@mail.com",
                "Bober",
                "Bobson",
                LocalDate.of(2000, 3, 3),
                "Shevchenka 7", "1234456788"));
    }

    private static UserDeleteResponse getDefaultDeleteResponse() {
        return new UserDeleteResponse("User is deleted.");
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
