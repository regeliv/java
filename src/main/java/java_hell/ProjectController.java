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
@Tag(name = "Projects")
@RequestMapping("/api/projects")
class ProjectController {
  private final ProjectRepository projectRepository;

  ProjectController(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @GetMapping
  @Operation(summary = "Return a list of all projects in the database")
  List<Project> all() {
    return projectRepository.findAll();
  }

  @PostMapping
  @Operation(summary = "Add a project to the database")
  Project createProject(@Valid @RequestBody Project project) {
    return projectRepository.save(project);
  }
}
