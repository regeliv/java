package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
class TaskController {
  private final TaskRepository taskRepository;

  TaskController(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @GetMapping
  List<Task> all() {
    return taskRepository.findAll();
  }

  @PostMapping
  Task createTask(@Valid @RequestBody Task task) {
    return taskRepository.save(task);
  }
}
