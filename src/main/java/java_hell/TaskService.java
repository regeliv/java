package java_hell;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
class TaskService {
  private final TaskRepository taskRepository;

  TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  List<Task> all() {
    return taskRepository.findAll();
  }

  Task createTask(Task task) {
    return taskRepository.save(task);
  }

  Task updateTask(Long id, Task updatedTask) {
    Task currentTask = taskRepository.findById(id).orElseThrow();

    currentTask.setTitle(updatedTask.getTitle());
    currentTask.setDescription(updatedTask.getDescription());
    currentTask.setPriority(updatedTask.getPriority());

    return taskRepository.save(currentTask);
  }

  void deleteTask(Long id) {
    Task task = taskRepository.findById(id).orElseThrow();

    if (task.getProject() != null) {
      throw new IllegalStateException("Cannot delete a task that is part of a project");
    }

    taskRepository.delete(task);

  }
}
