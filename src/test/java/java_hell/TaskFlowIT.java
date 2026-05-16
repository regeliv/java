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
class TaskFlowIT {
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired
  MockMvc mockMvc;

  @Test
  void taskFlow() throws Exception {
    String taskResponse = mockMvc.perform(post("/api/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "title": "Integration task",
              "description": "foo",
              "priority": "HIGH"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Integration task"))
        .andExpect(jsonPath("$.description").value("foo"))
        .andExpect(jsonPath("$.priority").value("HIGH"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Number taskId = com.jayway.jsonpath.JsonPath.read(taskResponse, "$.id");

    mockMvc.perform(get("/api/tasks/{id}", taskId.longValue()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(taskId.longValue()))
        .andExpect(jsonPath("$.title").value("Integration task"))
        .andExpect(jsonPath("$.description").value("foo"))
        .andExpect(jsonPath("$.priority").value("HIGH"));
  }
}
