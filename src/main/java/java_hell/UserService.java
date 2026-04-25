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
}
