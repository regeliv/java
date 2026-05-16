package java_hell;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserFlowIT {
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired
  MockMvc mockMvc;

  @Test
  void userFlow() throws Exception {
    String userResponse = mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "username": "integration-user"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("integration-user"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Number userId = com.jayway.jsonpath.JsonPath.read(userResponse, "$.id");

    mockMvc.perform(get("/api/users/{id}", userId.longValue()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.longValue()))
        .andExpect(jsonPath("$.username").value("integration-user"));
  }
}
