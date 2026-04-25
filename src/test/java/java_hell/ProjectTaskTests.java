package java_hell;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class ProjectTaskTests {
  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TaskRepository taskRepository;

  @Test
  void addManyTasksToProject() {
    Task task1 = new Task();
    task1.setTitle("task1");

    Task task2 = new Task();
    task2.setTitle("task2");

    Project proj = new Project();
    proj.setName("Project");
    proj.setDescription("...");

    taskRepository.saveAndFlush(task1);
    taskRepository.saveAndFlush(task2);

    projectRepository.saveAndFlush(proj);

    proj.addTask(task1);
    proj.addTask(task2);

    assertEquals(proj, task1.getProject());
    assertEquals(proj, task2.getProject());

    assertEquals(2, proj.getTasks().size());
  }

  @Test
  void addNullTask() {
    Project proj = new Project();
    proj.setName("Project");
    proj.setDescription("...");

    projectRepository.saveAndFlush(proj);

    assertThrows(IllegalArgumentException.class, () -> {
      proj.addTask(null);
    });

    assertEquals(0, proj.getTasks().size());
  }

  @Test
  void addTaskAlreadyAssignedToProject() {
    Project proj1 = new Project();
    proj1.setName("Project 2");
    proj1.setDescription("...");

    Project proj2 = new Project();
    proj2.setName("Project 2");
    proj2.setDescription("...");

    Task task = new Task();
    task.setTitle("task");

    projectRepository.saveAndFlush(proj1);
    projectRepository.saveAndFlush(proj2);

    taskRepository.saveAndFlush(task);

    proj1.addTask(task);

    assertEquals(proj1, task.getProject());
    assertEquals(1, proj1.getTasks().size());
    assertEquals(0, proj2.getTasks().size());

    proj2.addTask(task);

    assertEquals(proj2, task.getProject());
    assertEquals(0, proj1.getTasks().size());
    assertEquals(1, proj2.getTasks().size());
  }

  @Test
  void removeTask() {
    Task task = new Task();
    task.setTitle("task");

    Project proj = new Project();
    proj.setName("Project");
    proj.setDescription("...");

    taskRepository.saveAndFlush(task);

    projectRepository.saveAndFlush(proj);

    proj.addTask(task);

    assertEquals(proj, task.getProject());
    assertEquals(1, proj.getTasks().size());

    assertDoesNotThrow(() -> {
      proj.removeTask(task);
    });

    assertNull(task.getProject());
    assertEquals(0, proj.getTasks().size());
  }

  @Test
  void removeInvalidTask() {
    Task task = new Task();
    task.setTitle("task");

    Project proj = new Project();
    proj.setName("Project");
    proj.setDescription("...");

    taskRepository.saveAndFlush(task);

    projectRepository.saveAndFlush(proj);

    assertNull(task.getProject());
    assertEquals(0, proj.getTasks().size());

    assertThrows(IllegalArgumentException.class, () -> {
      proj.removeTask(task);
    });

    assertNull(task.getProject());
    assertEquals(0, proj.getTasks().size());
  }
}
