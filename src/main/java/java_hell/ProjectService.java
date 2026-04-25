package java_hell;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
class ProjectService {
  private final ProjectRepository projectRepository;

  ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  List<Project> all() {
    return projectRepository.findAll();
  }

  Project getById(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
  }

  Project createProject(Project project) {
    return projectRepository.save(project);
  }

  Project updateProject(Long id, Project updatedProject) {
    Project currentProject = projectRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

    currentProject.setDescription(updatedProject.getDescription());
    currentProject.setName(updatedProject.getName());

    return projectRepository.save(currentProject);
  }

  void deleteProject(Long id) {
    Project project = projectRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

    if (project.getTasks().size() != 0 || project.getUsers().size() != 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "Cannot delete a project if it has tasks or users assigned to it");
    }

    projectRepository.delete(project);
  }

}
