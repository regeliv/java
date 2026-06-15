package java_hell;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class ProjectFlowIT {
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired
  MockMvc mockMvc;

  @Test
  void createProject() throws Exception {
    String projectResponse = mockMvc.perform(post("/api/projects")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "name": "Integration project",
              "description": "foo"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Integration project"))
        .andExpect(jsonPath("$.description").value("foo"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Number projectId = com.jayway.jsonpath.JsonPath.read(projectResponse, "$.id");

    mockMvc.perform(get("/api/projects/{id}", projectId.longValue()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(projectId.longValue()))
        .andExpect(jsonPath("$.name").value("Integration project"))
        .andExpect(jsonPath("$.description").value("foo"));

    mockMvc.perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id", hasItem(projectId.intValue())));
  }

  @Test
  void updateProject() throws Exception {
    String response = mockMvc.perform(post("/api/projects")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { "name": "Original", "description": "old" }
            """))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    Number id = com.jayway.jsonpath.JsonPath.read(response, "$.id");

    mockMvc.perform(put("/api/projects/{id}", id.longValue())
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { "name": "Updated", "description": "new" }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated"))
        .andExpect(jsonPath("$.description").value("new"));

    mockMvc.perform(get("/api/projects/{id}", id.longValue()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated"));
  }

  @Test
  void deleteProject() throws Exception {
    String response = mockMvc.perform(post("/api/projects")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { "name": "To delete", "description": "foo" }
            """))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    Number id = com.jayway.jsonpath.JsonPath.read(response, "$.id");

    mockMvc.perform(delete("/api/projects/{id}", id.longValue()))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/projects/{id}", id.longValue()))
        .andExpect(status().isNotFound());
  }

  @Test
  void missingProject() throws Exception {
    long badId = 12345;

    mockMvc.perform(get("/api/projects/{id}", badId))
        .andExpect(status().isNotFound());

    mockMvc.perform(put("/api/projects/{id}", badId)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { "name": "x", "description": "y" }
            """))
        .andExpect(status().isNotFound());

    mockMvc.perform(delete("/api/projects/{id}", badId))
        .andExpect(status().isNotFound());
  }

}
