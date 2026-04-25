package java_hell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ProjectControllerTests {
  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TaskRepository taskRepository;

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

  @Test
  void updateProject() throws Exception {
    Project project = new Project();
    project.setName("My proj");
    project.setDescription("foo");

    Project savedProject = projectRepository.saveAndFlush(project);

    mockMvc.perform(put("/api/projects/{id}", savedProject.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "name": "My proj (but better)",
              "description": "bar"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("My proj (but better)"))
        .andExpect(jsonPath("$.description").value("bar"));

    Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();

    assertEquals("My proj (but better)", updatedProject.getName());
    assertEquals("bar", updatedProject.getDescription());
  }

  @Test
  void failToUpdateProjectWithInvalidId() throws Exception {
    assertThrows(Exception.class, () -> mockMvc.perform(put("/api/projects/{id}", 9999)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "name": "My proj (but better)",
              "description": "bar"
            }
            """)));

    assertEquals(0, projectRepository.count());
  }

  @Test
  void deleteProject() throws Exception {
    Project project = new Project();
    project.setName("My proj");
    project.setDescription("foo");

    Project savedProject = projectRepository.saveAndFlush(project);

    mockMvc.perform(delete("/api/projects/{id}", savedProject.getId()))
        .andExpect(status().isOk());

    assertEquals(0, projectRepository.count());
  }

  @Test
  void failToDeleteProjectWithTasksOrUsers() throws Exception {
    Project projectWithUser = new Project();
    projectWithUser.setName("My proj");
    projectWithUser.setDescription("foo");

    User user = new User();
    user.setUsername("user1");
    userRepository.saveAndFlush(user);
    projectWithUser.addUser(user);

    Project savedProjectWithUser = projectRepository.saveAndFlush(projectWithUser);

    Project projectWithTask = new Project();
    projectWithTask.setName("My proj 2");
    projectWithTask.setDescription("bar");

    Task task = new Task();
    task.setTitle("Task");
    task.setDescription("Foo");
    task.setPriority(TaskPriority.HIGH);
    projectWithTask.addTask(task);

    Project savedProjectWithTask = projectRepository.saveAndFlush(projectWithTask);
    taskRepository.saveAndFlush(task);

    assertThrows(Exception.class, () -> mockMvc.perform(delete("/api/projects/{id}", savedProjectWithUser.getId())));
    assertThrows(Exception.class, () -> mockMvc.perform(delete("/api/projects/{id}", savedProjectWithTask.getId())));

    assertEquals(2, projectRepository.count());
  }

  @Test
  void failToDeleteProjectWithInvalidId() throws Exception {
    assertThrows(Exception.class, () -> mockMvc.perform(delete("/api/projects/{id}", 9999)));

    assertEquals(0, projectRepository.count());
  }

}
