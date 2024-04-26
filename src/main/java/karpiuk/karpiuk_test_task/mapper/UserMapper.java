package karpiuk.karpiuk_test_task.mapper;

import karpiuk.karpiuk_test_task.configuration.MapperConfiguration;
import karpiuk.karpiuk_test_task.dto.response.UserRegistrationResponse;
import karpiuk.karpiuk_test_task.dto.response.UserSearchResponse;
import karpiuk.karpiuk_test_task.dto.response.UserUpdateResponse;
import karpiuk.karpiuk_test_task.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    UserRegistrationResponse toRegistrationResponse(User user);

    UserUpdateResponse toUpdateResponse(User user);

    UserSearchResponse toSearchResponse(User user);
}
