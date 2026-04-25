package java_hell;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
class UserService {
  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  List<User> all() {
    return userRepository.findAll();
  }

  User createUser(User user) {
    return userRepository.save(user);
  }

  User updateUser(Long id, User updatedUser) {
    User currentUser = userRepository.findById(id).orElseThrow();

    currentUser.setUsername(updatedUser.getUsername());

    return userRepository.save(currentUser);
  }

  void deleteUser(Long id) {
    User user = userRepository.findById(id).orElseThrow();

    if (user.getProjects().size() != 0) {
      throw new IllegalStateException("Cannot delete a user if they are part of a project");
    }

    userRepository.delete(user);
  }
}
