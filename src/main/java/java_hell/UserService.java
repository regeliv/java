package java_hell;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
class UserService {
  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  List<User> all() {
    return userRepository.findAll();
  }

  User getById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  User createUser(User user) {
    return userRepository.save(user);
  }

  User updateUser(Long id, User updatedUser) {
    User currentUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    currentUser.setUsername(updatedUser.getUsername());

    return userRepository.save(currentUser);
  }

  void deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if (user.getProjects().size() != 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete a user if they are part of a project");
    }

    userRepository.delete(user);
  }
}
