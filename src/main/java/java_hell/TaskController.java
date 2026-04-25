package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Tasks")
@RequestMapping("/api/tasks")
class TaskController {
  private final TaskService taskService;

  TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  @Operation(summary = "Return a list of all tasks in the database")
  List<Task> all() {
    return taskService.all();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Return the task with the specified id")
  Task getById(@Parameter(description = "ID of an existing task", example = "1") @PathVariable Long id) {
    return taskService.getById(id);
  }

  @PostMapping
  @Operation(summary = "Add a task to the database")
  Task createTask(@Valid @RequestBody Task task) {
    return taskService.createTask(task);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Modify task's properties")
  Task updateTask(@Parameter(description = "ID of an existing task", example = "1") @PathVariable Long id,
      @Valid @RequestBody Task task) {
    return taskService.updateTask(id, task);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a task")
  void deleteTask(@Parameter(description = "ID of an existing task", example = "1") @PathVariable Long id) {
    taskService.deleteTask(id);
  }
}
