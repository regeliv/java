package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
class UserController {
  private final UserRepository userRepository;

  UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  List<User> all() {
    return userRepository.findAll();
  }

  @PostMapping
  User createUser(@Valid @RequestBody User user) {
    return userRepository.save(user);
  }
}
