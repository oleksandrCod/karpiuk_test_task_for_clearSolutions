package karpiuk.karpiuk_test_task;

import java.time.LocalDate;
import karpiuk.karpiuk_test_task.model.User;
import karpiuk.karpiuk_test_task.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KarpiukTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(KarpiukTestTaskApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            for (int i = 1; i <= 10; i++) {
                User user = new User();
                user.setEmail("user" + i + "@example.com");
                user.setFirstName("FirstName" + i);
                user.setLastName("LastName" + i);
                user.setBirthDate(LocalDate.of(2003, 02, 02).minusYears(i));
                user.setAddress("Address" + i);
                user.setPhoneNumber("1234567890" + i);

                userRepository.save(user);
            }
        };
    }
}
