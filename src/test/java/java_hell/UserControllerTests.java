package java_hell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserControllerTests {
  @Autowired
  UserRepository userRepository;

  @Autowired
  MockMvc mockMvc;

  @Test
  void createUser() throws Exception {
    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "Foo"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Foo"));

    assertEquals(1, userRepository.count());
  }

  @Test
  void failToCreateUser() throws Exception {
    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "user_name": "Foo"
            }
            """))
        .andExpect(status().isBadRequest());

    assertEquals(0, userRepository.count());
  }

  @Test
  void getAllUsers() throws Exception {
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));

    User user1 = new User();
    user1.setUsername("user1");

    User user2 = new User();
    user2.setUsername("user2");

    userRepository.saveAndFlush(user1);
    userRepository.saveAndFlush(user2);

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[1].username").value("user2"));

    assertEquals(2, userRepository.count());

  }

}
