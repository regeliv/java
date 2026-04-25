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
class TaskControllerTests {
  @Autowired
  TaskRepository taskRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  MockMvc mockMvc;

  @Test
  void createTask() throws Exception {
    mockMvc.perform(post("/api/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "title": "Task",
              "description": "Foo",
              "priority": "HIGH"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Task"))
        .andExpect(jsonPath("$.description").value("Foo"))
        .andExpect(jsonPath("$.priority").value("HIGH"));

    assertEquals(1, taskRepository.count());
  }

  @Test
  void failToCreateTask() throws Exception {
    mockMvc.perform(post("/api/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "title": "Task",
              "description": "Desc",
              "priority": "MEDIUM-HIGH?"
            }
            """))
        .andExpect(status().isBadRequest());

    assertEquals(0, taskRepository.count());
  }

  @Test
  void getAllTasks() throws Exception {
    mockMvc.perform(get("/api/tasks"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));

    Task task1 = new Task();
    task1.setTitle("Task 1");
    task1.setDescription("Foo");
    task1.setPriority(TaskPriority.MEDIUM);

    Task task2 = new Task();
    task2.setTitle("Task 2");
    task2.setDescription("Bar");
    task2.setPriority(TaskPriority.LOW);

    taskRepository.saveAndFlush(task1);
    taskRepository.saveAndFlush(task2);

    mockMvc.perform(get("/api/tasks"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].title").value("Task 1"))
        .andExpect(jsonPath("$[0].description").value("Foo"))
        .andExpect(jsonPath("$[0].priority").value("MEDIUM"))
        .andExpect(jsonPath("$[1].title").value("Task 2"))
        .andExpect(jsonPath("$[1].description").value("Bar"))
        .andExpect(jsonPath("$[1].priority").value("LOW"));

    assertEquals(2, taskRepository.count());

  }

  @Test
  void updateTask() throws Exception {
    Task task = new Task();
    task.setTitle("Task");
    task.setDescription("Foo");
    task.setPriority(TaskPriority.HIGH);

    Task savedTask = taskRepository.saveAndFlush(task);

    mockMvc.perform(put("/api/tasks/{id}", savedTask.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "title": "Task 2",
              "description": "Bar",
              "priority": "LOW"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Task 2"))
        .andExpect(jsonPath("$.description").value("Bar"))
        .andExpect(jsonPath("$.priority").value("LOW"));

    Task updatedTask = taskRepository.findById(savedTask.getId()).orElseThrow();

    assertEquals("Task 2", updatedTask.getTitle());
    assertEquals("Bar", updatedTask.getDescription());
    assertEquals(TaskPriority.LOW, updatedTask.getPriority());
  }

  @Test
  void failToUpdateTaskWithInvalidId() throws Exception {
    assertThrows(Exception.class, () -> mockMvc.perform(put("/api/tasks/{id}", 9999)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "title": "Task 2",
              "description": "Bar",
              "priority": "LOW"
            }
            """)));

    assertEquals(0, taskRepository.count());
  }

  @Test
  void deleteTask() throws Exception {
    Task task = new Task();
    task.setTitle("Task");
    task.setDescription("Foo");
    task.setPriority(TaskPriority.HIGH);

    Task savedTask = taskRepository.saveAndFlush(task);

    mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()))
        .andExpect(status().isOk());

    assertEquals(0, taskRepository.count());
  }

  @Test
  void failToDeleteTaskThatIsPartOfAProject() throws Exception {
    Project project = new Project();
    project.setName("My proj");
    project.setDescription("foo");
    projectRepository.saveAndFlush(project);

    Task task = new Task();
    task.setTitle("Task");
    task.setDescription("Foo");
    task.setPriority(TaskPriority.HIGH);
    project.addTask(task);
    taskRepository.saveAndFlush(task);

    assertThrows(Exception.class, () -> mockMvc.perform(delete("/api/tasks/{id}", task.getId())));

    assertEquals(1, taskRepository.count());
  }

  @Test
  void failToDeleteTaskWithInvalidId() throws Exception {
    assertThrows(Exception.class, () -> mockMvc.perform(delete("/api/tasks/{id}", 9999)));

    assertEquals(0, taskRepository.count());
  }

}
