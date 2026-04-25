package java_hell;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
class ProjectController {
  private final ProjectRepository projectRepository;

  ProjectController(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @GetMapping
  List<Project> all() {
    return projectRepository.findAll();
  }

  @PostMapping
  Project createProject(@Valid @RequestBody Project project) {
    return projectRepository.save(project);
  }
}
