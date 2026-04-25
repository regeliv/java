package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
class UserController {
  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "Return a list of all users in the database")
  List<User> all() {
    return userService.all();
  }

  @PostMapping
  @Operation(summary = "Add a user to the database")
  User createUser(@Valid @RequestBody User user) {
    return userService.createUser(user);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Modify user's properties")
  User updateUser(@Parameter(description = "ID of an existing user", example = "1") @PathVariable Long id,
      @Valid @RequestBody User user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a user")
  void deleteUser(@Parameter(description = "ID of an existing user", example = "1") @PathVariable Long id) {
    userService.deleteUser(id);
  }
}
