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
class ProjectControllerTests {
  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  MockMvc mockMvc;

  @Test
  void createProject() throws Exception {
    mockMvc.perform(post("/api/projects")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "name": "Proj",
              "description": "Desc"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Proj"))
        .andExpect(jsonPath("$.description").value("Desc"));

    assertEquals(1, projectRepository.count());
  }

  @Test
  void failToCreateProject() throws Exception {
    mockMvc.perform(post("/api/projects")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "wrong_name": "Proj",
              "description": "Desc"
            }
            """))
        .andExpect(status().isBadRequest());

    assertEquals(0, projectRepository.count());
  }

  @Test
  void getAllProjects() throws Exception {
    mockMvc.perform(get("/api/projects"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));

    Project project1 = new Project();
    project1.setName("My project 1");
    project1.setDescription("Foo");

    Project project2 = new Project();
    project2.setName("My project 2");
    project2.setDescription("Bar");

    projectRepository.saveAndFlush(project1);
    projectRepository.saveAndFlush(project2);

    mockMvc.perform(get("/api/projects"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("My project 1"))
        .andExpect(jsonPath("$[0].description").value("Foo"))
        .andExpect(jsonPath("$[1].name").value("My project 2"))
        .andExpect(jsonPath("$[1].description").value("Bar"));

    assertEquals(2, projectRepository.count());

  }

}
