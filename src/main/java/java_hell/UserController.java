package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
class UserController {
  private final UserRepository userRepository;

  UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  @Operation(summary = "Return a list of all users in the database")
  List<User> all() {
    return userRepository.findAll();
  }

  @PostMapping
  @Operation(summary = "Add a user to the database")
  User createUser(@Valid @RequestBody User user) {
    return userRepository.save(user);
  }
}
