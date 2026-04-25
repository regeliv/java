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
@Tag(name = "Projects")
@RequestMapping("/api/projects")
class ProjectController {
  private final ProjectService projectService;

  ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @GetMapping
  @Operation(summary = "Return a list of all projects in the database")
  List<Project> all() {
    return projectService.all();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Return the project with the specified id")
  Project getById(@Parameter(description = "ID of an existing project", example = "1") @PathVariable Long id) {
    return projectService.getById(id);
  }

  @PostMapping
  @Operation(summary = "Add a project to the database")
  Project createProject(@Valid @RequestBody Project project) {
    return projectService.createProject(project);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Modify project's properties")
  Project updateProject(@Parameter(description = "ID of an existing project", example = "1") @PathVariable Long id,
      @Valid @RequestBody Project project) {
    return projectService.updateProject(id, project);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a project")
  void deleteProject(@Parameter(description = "ID of an existing project", example = "1") @PathVariable Long id) {
    projectService.deleteProject(id);
  }
}
