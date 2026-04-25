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
}
