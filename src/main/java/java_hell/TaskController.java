package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Tasks")
@RequestMapping("/api/tasks")
class TaskController {
  private final TaskRepository taskRepository;

  TaskController(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @GetMapping
  @Operation(summary = "Return a list of all tasks in the database")
  List<Task> all() {
    return taskRepository.findAll();
  }

  @PostMapping
  @Operation(summary = "Add a task to the database")
  Task createTask(@Valid @RequestBody Task task) {
    return taskRepository.save(task);
  }
}
