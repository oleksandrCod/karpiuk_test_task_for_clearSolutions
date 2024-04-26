package karpiuk.karpiuk_test_task.service.impl;

import java.time.LocalDate;
import java.util.List;
import karpiuk.karpiuk_test_task.dto.request.UserMinorUpdateRequest;
import karpiuk.karpiuk_test_task.dto.request.UserRegistrationRequest;
import karpiuk.karpiuk_test_task.dto.request.UserSearchByBirthDateRangeRequest;
import karpiuk.karpiuk_test_task.dto.request.UserUpdateAllRequest;
import karpiuk.karpiuk_test_task.dto.response.UserDeleteResponse;
import karpiuk.karpiuk_test_task.dto.response.UserRegistrationResponse;
import karpiuk.karpiuk_test_task.dto.response.UserSearchResponse;
import karpiuk.karpiuk_test_task.dto.response.UserUpdateResponse;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.DuplicateEmailException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.InvalidBirthDateRangeException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.UserNotAdultException;
import karpiuk.karpiuk_test_task.exception.handler.exceptions.UserNotFoundException;
import karpiuk.karpiuk_test_task.mapper.UserMapper;
import karpiuk.karpiuk_test_task.model.User;
import karpiuk.karpiuk_test_task.repository.UserRepository;
import karpiuk.karpiuk_test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Setter
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_ADULT_ERROR_MESSAGE =
            "You cannot continue registration, your age must be greater than or equal to ";
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Can't find user with input id:";
    private static final String REGISTRATION_EXCEPTION_ERROR_MESSAGE = "Provided email is already in use: ";
    private static final String USER_DELETED_MESSAGE = "User was deleted successfully";
    private static final String DATE_RANGE_EXCEPTION_MESSAGE = "'From' date must be less than 'To' date";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${valid.user.registration.required.age}")
    private int requiredAge;

    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest request) {
        log.info("Creating new user {}", request.email());

        validateUser(request);

        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setBirthDate(request.birthDate());

        user.setAddress(request.address());
        user.setPhoneNumber(request.phoneNumber());

        log.info("User created successfully.");

        return userMapper.toRegistrationResponse(userRepository.save(user));
    }

    @Override
    public UserUpdateResponse minorUpdate(UserMinorUpdateRequest updateRequest, Long id) {
        User userToUpdate = fetchUserById(id);

        log.info("Perform minor updates for user {}", userToUpdate.getEmail());

        if (updateRequest.email() != null) {
            userToUpdate.setEmail(updateRequest.email());
        }
        if (updateRequest.firstName() != null) {
            userToUpdate.setFirstName(updateRequest.firstName());
        }
        if (updateRequest.lastName() != null) {
            userToUpdate.setLastName(updateRequest.lastName());
        }
        if (updateRequest.birthDate() != null) {
            userToUpdate.setBirthDate(updateRequest.birthDate());
        }
        if (updateRequest.address() != null) {
            userToUpdate.setAddress(updateRequest.address());
        }
        if (updateRequest.phoneNumber() != null) {
            userToUpdate.setPhoneNumber(updateRequest.phoneNumber());
        }

        log.info("Minor updates performed successfully for user {}", userToUpdate.getEmail());

        return userMapper.toUpdateResponse(userRepository.save(userToUpdate));
    }

    @Override
    public UserUpdateResponse updateAll(UserUpdateAllRequest request, Long id) {
        User userToUpdate = fetchUserById(id);

        log.info("Perform update all fields for user {}", userToUpdate.getEmail());

        userToUpdate.setEmail(request.email());
        userToUpdate.setFirstName(request.firstName());
        userToUpdate.setLastName(request.lastName());
        userToUpdate.setBirthDate(request.birthDate());
        userToUpdate.setAddress(request.address());
        userToUpdate.setPhoneNumber(request.phoneNumber());

        log.info("All updates performed successfully for user {}", userToUpdate.getEmail());

        return userMapper.toUpdateResponse(userRepository.save(userToUpdate));
    }

    @Override
    public UserDeleteResponse deleteById(Long id) {

        log.info("Deleting user with id {}", id);

        fetchUserById(id);

        userRepository.deleteById(id);

        log.info("User with id {} is deleted", id);

        return new UserDeleteResponse(USER_DELETED_MESSAGE);
    }

    @Override
    public List<UserSearchResponse> findByBirthDateRange(UserSearchByBirthDateRangeRequest request) {
        LocalDate from = request.from();
        LocalDate to = request.to();

        validateDataRange(from, to);

        log.info("Finding users with birth date range from {} to {}", from, to);

        return userRepository.findAllByBirthDateBetween(from, to).stream()
                .map(userMapper::toSearchResponse).toList();
    }


    private void validateUser(UserRegistrationRequest request) {
        log.info("Validating user {}", request.email());

        LocalDate today = LocalDate.now();
        int userAge = today.minusYears(request.birthDate().getYear()).getYear();
        if (userAge < requiredAge) {

            log.warn("User age {} is not valid", userAge);

            throw new UserNotAdultException(USER_NOT_ADULT_ERROR_MESSAGE + requiredAge);
        }
        log.info("Age validated for user {}", request.email());

        if (userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {

            log.warn("User email is already in use: {}", request.email());

            throw new DuplicateEmailException(REGISTRATION_EXCEPTION_ERROR_MESSAGE + request.email());
        }

        log.info("User {} validated successfully", request.email());
    }

    private User fetchUserById(Long id) {

        log.info("Fetching user by id {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE + id));
    }

    private static void validateDataRange(LocalDate from, LocalDate to) {
        log.info("Validating data range {} to {}", from, to);

        if (from.isAfter(to)) {
            throw new InvalidBirthDateRangeException(DATE_RANGE_EXCEPTION_MESSAGE);
        }
    }
}
