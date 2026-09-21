package config;

import entity.Role;
import entity.User;
import repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("Admin", "admin@gmail.com", passwordEncoder.encode("123456"), "0123456789", Role.AD));
            userRepository.save(new User("Staff", "staff@gmail.com", passwordEncoder.encode("123456"), "0987654321", Role.STAFF));
        }
    }
}