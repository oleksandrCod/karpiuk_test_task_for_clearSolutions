package karpiuk.karpiuk_test_task.controller;

import jakarta.validation.Valid;
import java.util.List;
import karpiuk.karpiuk_test_task.dto.request.UserMinorUpdateRequest;
import karpiuk.karpiuk_test_task.dto.request.UserRegistrationRequest;
import karpiuk.karpiuk_test_task.dto.request.UserSearchByBirthDateRangeRequest;
import karpiuk.karpiuk_test_task.dto.request.UserUpdateAllRequest;
import karpiuk.karpiuk_test_task.dto.response.UserDeleteResponse;
import karpiuk.karpiuk_test_task.dto.response.UserRegistrationResponse;
import karpiuk.karpiuk_test_task.dto.response.UserSearchResponse;
import karpiuk.karpiuk_test_task.dto.response.UserUpdateResponse;
import karpiuk.karpiuk_test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(
            @RequestBody @Valid UserRegistrationRequest request) {

        log.info("Received request to register user {}", request.email());

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PatchMapping("/minor-update/{id}")
    public ResponseEntity<UserUpdateResponse> minorUpdate(
            @RequestBody @Valid UserMinorUpdateRequest updateRequest, @PathVariable Long id) {

        log.info("Received updateRequest to for minor update user with id {}", id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.minorUpdate(updateRequest, id));
    }

    @PutMapping("/update-all/{id}")
    public ResponseEntity<UserUpdateResponse> fullUpdate(
            @RequestBody @Valid UserUpdateAllRequest request, @PathVariable Long id) {

        log.info("Received request to update user with id {}", id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateAll(request, id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> findAllByBirthDateRange(
            @RequestBody @Valid UserSearchByBirthDateRangeRequest request) {

        log.info("Received request to find all users with birth date range {}", request);

        return ResponseEntity.status(HttpStatus.FOUND).body(userService.findByBirthDateRange(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDeleteResponse> delete(@PathVariable Long id) {

        log.info("Received request to delete user with id {}", id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteById(id));
    }
}
